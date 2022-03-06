package com.nulp.fetchproductdata.controllers;

import com.nulp.fetchproductdata.api.CatalogApi;
import com.nulp.fetchproductdata.api.response.Catalog;
import com.nulp.fetchproductdata.api.response.Category;
import com.nulp.fetchproductdata.service.RetrieveCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryTreeController implements CatalogApi {

    private final RetrieveCategoriesService categoriesService;
    public Catalog getRootCatalog(boolean reindex) {
        return categoriesService.getCatalog(reindex);
    }

    public Category getCategoryByTitle(String title) {
        return null;
    }
}
