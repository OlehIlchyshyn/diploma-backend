package com.nulp.fetchproductdata.service.initialization;

import com.nulp.fetchproductdata.config.Properties;
import com.nulp.fetchproductdata.mapping.IdMapperService;
import com.nulp.fetchproductdata.model.Category;
import com.nulp.fetchproductdata.model.ConversionRate;
import com.nulp.fetchproductdata.model.Product;
import com.nulp.fetchproductdata.parser.rozetka.categories.RozetkaCategoriesParser;
import com.nulp.fetchproductdata.repository.CategoryRepository;
import com.nulp.fetchproductdata.repository.ConversionRateRepository;
import com.nulp.fetchproductdata.service.ConversionService;
import com.nulp.fetchproductdata.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
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
  private final PriceHistoryService priceHistoryService;
  private final ConversionService conversionService;
  private final ConversionRateRepository conversionRateRepository;
  private final ModelMapper modelMapper;
  private final Properties properties;

  private final List<String> skippedCategories = List.of("Знижені в ціні товари");

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
    categories.forEach(
        category ->
            category
                .getProducts()
                .forEach(
                    product ->
                        priceHistoryService.addEntryToPriceHistory(
                            product.getId(), product.getPriceList())));
  }

  private List<com.nulp.fetchproductdata.model.Category> loadCategoriesAndProducts() {
    List<com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category>
        rootCategories = categoriesParser.fetchCategoriesFromApi();
    rootCategories.removeIf(
        category ->
            skippedCategories.contains(category.getTitle())
                || category.getChildren().stream()
                    .anyMatch(subcategory -> skippedCategories.contains(subcategory.getTitle())));

    if (properties.getCategoriesLimit() != null) {
      rootCategories = rootCategories.subList(0, properties.getCategoriesLimit());
    }

    for (var rootCategory : rootCategories) {
      idMapperService.addRozetkaCategoryEntry(
          rootCategory.getTitle(), rootCategory.getCategoryId());
      for (var category : rootCategory.getChildren()) {
        idMapperService.addRozetkaCategoryEntry(category.getTitle(), category.getCategoryId());
      }
    }

    List<com.nulp.fetchproductdata.model.Category> categories = mapToModel(rootCategories);

    for (var rootCategory : categories) {
      List<Product> rootCategoryProducts = new LinkedList<>();
      for (var category : rootCategory.getSubCategories()) {
        int subCategoryId;
        try {
          subCategoryId = idMapperService.getRozetkaCategoryIdByTitle(category.getTitle());
        } catch (NoSuchElementException exception) {
          log.warn("No rozetka id was found for category title: " + category.getTitle());
          continue;
        }
        var subCategoryProducts = rozetkaProductListService.getProductsByCategoryId(subCategoryId);
        rootCategoryProducts.addAll(subCategoryProducts);
        category.setProducts(subCategoryProducts);

        // flatten the hierarchy to two levels
        category.setSubCategories(null);
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
