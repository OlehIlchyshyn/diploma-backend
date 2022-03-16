package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.model.Category;
import com.nulp.fetchproductdata.model.Product;
import com.nulp.fetchproductdata.parser.rozetka.categories.RozetkaCategoriesParser;
import com.nulp.fetchproductdata.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitializationService {

  private final RozetkaCategoriesParser categoriesParser;
  private final IdMapperService idMapperService;
  private final ProductListService productListService;
  private final CategoryRepository categoryRepository;
  private final ModelMapper modelMapper;

  public List<Category> initCategories() {
    categoryRepository.deleteAll();
    List<Category> categories = loadCategoriesAndProducts();
    categoryRepository.saveAll(categories);
    return categories;
  }

  private List<com.nulp.fetchproductdata.model.Category> loadCategoriesAndProducts() {
    List<com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category>
        rootCategories = categoriesParser.fetchCategoriesFromApi();

    // todo remove temporary trim of the categories
    rootCategories = rootCategories.subList(0, 1);

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
        int subCategoryId = idMapperService.getRozetkaCategoryIdByTitle(category.getTitle());
        var subCategoryProducts = productListService.getProductsByCategoryId(subCategoryId);
        rootCategoryProducts.addAll(subCategoryProducts);
        category.setProducts(subCategoryProducts);
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