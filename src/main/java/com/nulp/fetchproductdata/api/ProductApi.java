package com.nulp.fetchproductdata.api;

import com.nulp.fetchproductdata.api.response.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@RequestMapping("/api/products")
public interface ProductApi {

  @GetMapping
  Page<Product> getAllProducts(
      @RequestParam(name = "categoryId", required = false) Long categoryId, Pageable pageable);

  @GetMapping("/{productId}")
  Product getProductById(@PathVariable Long productId);

  @GetMapping("/search")
  Set<Product> findProductsByTitle(@RequestParam String query);
}
