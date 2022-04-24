package com.nulp.fetchproductdata.controllers;

import com.nulp.fetchproductdata.api.CategoryApi;
import com.nulp.fetchproductdata.api.response.Category;
import com.nulp.fetchproductdata.service.RetrieveCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

  private final RetrieveCategoriesService categoriesService;

  @Override
  public Category getCategoryById(Long categoryId) {
    return categoriesService.getCategoryById(categoryId);
  }
}
