package com.nulp.fetchproductdata.service.initialization;

import com.nulp.fetchproductdata.config.Properties;
import com.nulp.fetchproductdata.mapping.IdMapperService;
import com.nulp.fetchproductdata.model.Category;
import com.nulp.fetchproductdata.model.ConversionRate;
import com.nulp.fetchproductdata.model.Product;
import com.nulp.fetchproductdata.parser.rozetka.categories.RozetkaCategoriesParser;
import com.nulp.fetchproductdata.repository.CategoryRepository;
import com.nulp.fetchproductdata.repository.ConversionRateRepository;
import com.nulp.fetchproductdata.repository.ProductRepository;
import com.nulp.fetchproductdata.service.ConversionService;
import com.nulp.fetchproductdata.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitializationService {

  private final InitPriceProvidersService initProvidersService;
  private final RozetkaCategoriesParser categoriesParser;
  private final IdMapperService idMapperService;
  private final RozetkaProductListService rozetkaProductListService;
  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final PriceHistoryService priceHistoryService;
  private final ConversionService conversionService;
  private final ConversionRateRepository conversionRateRepository;
  private final ModelMapper modelMapper;
  private final Properties properties;

  private final List<String> skippedCategories =
      List.of(
          "Товари для геймерів",
          "Інструменти та автотовари",
          "Знижені в ціні товари",
          "Товари з уцінкою",
          "PlayStation",
          "Зовнішні жорсткі диски",
          "Портативна акустика",
          "Послуги",
          "Джерела безперебійного живлення",
          "Звукові карти",
          "Жорсткі диски та дискові масиви",
          "Блоки живлення",
          "Корпуси",
          "Материнські плати",
          "Меблі для ванної кімнати",
          "Килими",
          "Господарські товари",
          "Церковне начиння",
          "Декор для дому",
          "Бачки для унітазу",
          "Сушарки для рук",
          "Будівельні матеріали",
          "Панелі для ванн",
          "Душові кабіни та стінки",
          "Кухня",
          "Догляд та прибирання",
          "Інсталяції та комплектуючі",
          "Ванни, бокси, душові",
          "Кераміка");
  private final List<String> categoriesWithSubchilds =
      List.of(
          "Аксесуари для ноутбуків і ПК",
          "Комплектуючi",
          "Аксесуари до мобільних телефонів",
          "Телевізори та аксесуари",
          "Фото та відео",
          "Аудіо та домашні кінотеатри",
          "Портативна електроніка",
          "Комп'ютери",
          "Мийки, змішувачі, сифони",
          "Інвентар для дому та офісу",
          "Меблі",
          "Кухня",
          "Догляд та прибирання",
          "Для краси та здоров'я",
          "Сантехніка та ванна кімната",
          "Посуд",
          "Обладнання");

  @Transactional
  public List<Category> initCategories() {
    categoryRepository.deleteAll();
    initProvidersService.initProviders();
    List<Category> categories = loadCategoriesAndProducts();
    categoryRepository.saveAll(categories);
    savePricesToHistory(categories);
    return categories;
  }

  public List<ConversionRate> initConversionRates() {
    conversionRateRepository.deleteAll();
    List<ConversionRate> conversionRates = conversionService.getAllRates();
    conversionRateRepository.saveAll(conversionRates);
    return conversionRates;
  }

  private void savePricesToHistory(List<Category> categories) {
    productRepository
        .findAll()
        .forEach(
            product ->
                priceHistoryService.addEntryToPriceHistory(
                    product.getId(), product.getPriceList()));
  }

  private List<com.nulp.fetchproductdata.model.Category> loadCategoriesAndProducts() {
    List<com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category>
        rootCategories = categoriesParser.fetchCategoriesFromApi();
    rootCategories.removeIf(category -> skippedCategories.contains(category.getTitle()));
    rootCategories.forEach(
        category -> {
          if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            category
                .getChildren()
                .removeIf(subcategory -> skippedCategories.contains(subcategory.getTitle()));
          }
        });
    if (properties.getCategoriesLimit() != null) {
      rootCategories = rootCategories.subList(0, properties.getCategoriesLimit());
    }

    for (var rootCategory : rootCategories) {
      idMapperService.addRozetkaCategoryEntry(
          rootCategory.getTitle(), rootCategory.getCategoryId());
      for (var category : rootCategory.getChildren()) {
        idMapperService.addRozetkaCategoryEntry(category.getTitle(), category.getCategoryId());
        if (categoriesWithSubchilds.contains(category.getTitle())) {
          for (var subcategory : category.getChildren()) {
            idMapperService.addRozetkaCategoryEntry(
                subcategory.getTitle(), subcategory.getCategoryId());
          }
        }
      }
    }

    List<com.nulp.fetchproductdata.model.Category> categories = mapToModel(rootCategories);

    for (var rootCategory : categories) {
      Set<Product> rootCategoryProducts = new LinkedHashSet<>();
      for (var category : rootCategory.getSubCategories()) {
        Set<Product> categoryProducts = new LinkedHashSet<>();
        if (categoriesWithSubchilds.contains(category.getTitle())) {
          for (var subCategory : category.getSubCategories()) {
            int subCategoryId;
            try {
              subCategoryId = idMapperService.getRozetkaCategoryIdByTitle(subCategory.getTitle());
            } catch (NoSuchElementException exception) {
              log.warn("No rozetka id was found for category title: " + subCategory.getTitle());
              continue;
            }
            var subCategoryProducts =
                rozetkaProductListService.getProductsByCategoryId(subCategoryId);
            subCategory.setProducts(subCategoryProducts);
            categoryProducts.addAll(subCategoryProducts);

            // stop the hierarchy
            subCategory.setSubCategories(null);
          }
        } else {
          // flatten the hierarchy to two levels
          category.setSubCategories(null);
        }

        int categoryId;
        try {
          categoryId = idMapperService.getRozetkaCategoryIdByTitle(category.getTitle());
        } catch (NoSuchElementException exception) {
          log.warn("No rozetka id was found for category title: " + category.getTitle());
          continue;
        }
        categoryProducts.addAll(rozetkaProductListService.getProductsByCategoryId(categoryId));
        category.setProducts(categoryProducts);
        rootCategoryProducts.addAll(categoryProducts);
      }
      rootCategory.setProducts(rootCategoryProducts);
    }

    return categories;
  }

  private List<com.nulp.fetchproductdata.model.Category> mapToModel(
      List<com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category>
          rootCategories) {
    return rootCategories.stream()
        .map(category -> modelMapper.map(category, com.nulp.fetchproductdata.model.Category.class))
        .collect(Collectors.toList());
  }
}
