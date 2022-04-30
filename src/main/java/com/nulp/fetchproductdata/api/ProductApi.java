package com.nulp.fetchproductdata.api;

import com.nulp.fetchproductdata.api.response.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@RequestMapping("/api/products")
public interface ProductApi {

  @GetMapping("/")
  Set<Product> getAllProducts();

  @GetMapping("/categories/{categoryId}")
  Set<Product> getProductsByCategory(@PathVariable Long categoryId);

  @GetMapping("/{productId}")
  Product getProductById(@PathVariable Long productId);

  @GetMapping("/search")
  Set<Product> findProductsByTitle(@RequestParam String query);
}
