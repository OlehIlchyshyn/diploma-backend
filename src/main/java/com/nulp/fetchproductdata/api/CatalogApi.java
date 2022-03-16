package com.nulp.fetchproductdata.api;

import com.nulp.fetchproductdata.api.response.Catalog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/catalog")
public interface CatalogApi {
  @GetMapping("/full")
  Catalog getFullCatalog(@RequestParam(value = "reindex", required = false) boolean reindex);

  @GetMapping("/basic")
  Catalog getCatalog();
}
