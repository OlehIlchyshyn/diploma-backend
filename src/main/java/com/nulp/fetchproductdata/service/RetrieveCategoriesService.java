package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.api.response.Catalog;
import com.nulp.fetchproductdata.api.response.Category;
import com.nulp.fetchproductdata.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrieveCategoriesService {

  private final CategoryRepository categoryRepository;
  private final ModelMapper modelMapper;

  /**
   * Retrieve catalog with categories and subcategories data, excluding the product list.
   *
   * @return catalog, consisting of categories and subcategories.
   */
  @Transactional
  public Catalog getFullCatalog() {
    return new Catalog(mapToResponse(categoryRepository.findAllParentCategories()));
  }

  /**
   * Retrieve catalog with categories and subcategories data, excluding the product list.
   *
   * @return catalog, consisting of categories and subcategories.
   */
  public Catalog getSimpleCatalog() {
    return new Catalog(
        mapToResponseWithoutProductData(
            categoryRepository.findAllParentCategoriesWithSubcategories()));
  }

  @Transactional
  public Category getCategoryById(Long categoryId) {
    return mapToResponse(categoryRepository.getById(categoryId));
  }

  private Category mapToResponse(com.nulp.fetchproductdata.model.Category category) {
    return modelMapper.map(category, Category.class);
  }

  private Set<Category> mapToResponse(Set<com.nulp.fetchproductdata.model.Category> categories) {
    return categories.stream().map(this::mapToResponse).collect(Collectors.toSet());
  }

  private Set<Category> mapToResponseWithoutProductData(
      Set<com.nulp.fetchproductdata.model.Category> categories) {
    return categories.stream()
        .map(
            category -> {
              Category categoryResponse = new Category();
              categoryResponse.setId(category.getId());
              categoryResponse.setTitle(category.getTitle());
              categoryResponse.setSubCategories(
                  category.getSubCategories().stream()
                      .map(
                          subcategory -> {
                            Category subcategoryResponse = new Category();
                            subcategoryResponse.setId(subcategory.getId());
                            subcategoryResponse.setTitle(subcategory.getTitle());
                            if (subcategory.getSubCategories() != null) {
                              subcategoryResponse.setSubCategories(
                                  subcategory.getSubCategories().stream()
                                      .map(
                                          subsubcategory -> {
                                            Category subsubcategoryResponse = new Category();
                                            subsubcategoryResponse.setId(subsubcategory.getId());
                                            subsubcategoryResponse.setTitle(
                                                subsubcategory.getTitle());
                                            return subsubcategoryResponse;
                                          })
                                      .collect(Collectors.toSet()));
                            }
                            return subcategoryResponse;
                          })
                      .collect(Collectors.toSet()));
              return categoryResponse;
            })
        .collect(Collectors.toSet());
  }
}
