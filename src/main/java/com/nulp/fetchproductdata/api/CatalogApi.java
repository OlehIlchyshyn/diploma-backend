package com.nulp.fetchproductdata.api;

import com.nulp.fetchproductdata.api.response.Catalog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/catalog")
public interface CatalogApi {
    @GetMapping("/")
    Catalog getRootCatalog(
            @RequestParam(value = "reindex", required = false) boolean reindex,
            @RequestParam(value = "includeSubcategories", required = false) boolean includeSubcategories,
            @RequestParam(value = "includeProductList", required = false) boolean includeProductList);
}
