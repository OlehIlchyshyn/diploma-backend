package com.nulp.fetchproductdata.controllers;

import com.nulp.fetchproductdata.api.CatalogApi;
import com.nulp.fetchproductdata.api.response.Catalog;
import com.nulp.fetchproductdata.service.InitializationService;
import com.nulp.fetchproductdata.service.RetrieveCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryTreeController implements CatalogApi {

  private final RetrieveCategoriesService categoriesService;
  private final InitializationService initializationService;

  @Override
  public Catalog getFullCatalog(boolean reindex) {
    if (reindex) {
      initializationService.initCategories();
    }
    return categoriesService.getFullCatalog();
  }

  @Override
  public Catalog getCatalog() {
    return categoriesService.getSimpleCatalog();
  }
}
