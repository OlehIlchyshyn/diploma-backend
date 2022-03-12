package com.nulp.fetchproductdata.controllers;

import com.nulp.fetchproductdata.api.CatalogApi;
import com.nulp.fetchproductdata.api.response.Catalog;
import com.nulp.fetchproductdata.service.RetrieveCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryTreeController implements CatalogApi {

  private final RetrieveCategoriesService categoriesService;

  public Catalog getRootCatalog(
      boolean reindex, boolean includeSubcategories, boolean includeProductList) {
    return categoriesService.getCatalog(reindex, includeSubcategories, includeProductList);
  }
}
