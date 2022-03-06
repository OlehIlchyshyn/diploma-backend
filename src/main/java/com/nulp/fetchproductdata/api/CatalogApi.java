package com.nulp.fetchproductdata.api;

import com.nulp.fetchproductdata.api.response.Catalog;
import com.nulp.fetchproductdata.api.response.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/catalog")
public interface CatalogApi {
    @GetMapping("/")
    Catalog getRootCatalog(@RequestParam(value = "reindex", required = false) boolean reindex);
    Category getCategoryByTitle(String title);
}
