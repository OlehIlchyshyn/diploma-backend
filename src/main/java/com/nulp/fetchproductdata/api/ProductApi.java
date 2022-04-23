package com.nulp.fetchproductdata.api;

import com.nulp.fetchproductdata.api.response.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/products")
public interface ProductApi {

  @GetMapping("/")
  List<Product> getAllProducts();

  @GetMapping("/categories/{categoryId}")
  List<Product> getProductsByCategory(@PathVariable Long categoryId);

  @GetMapping("/{productId}")
  Product getProductById(@PathVariable Long productId);

  @GetMapping("/search")
  List<Product> findProductsByTitle(@RequestParam String query);
}
