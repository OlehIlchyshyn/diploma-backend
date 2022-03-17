package com.nulp.fetchproductdata.api;

import com.nulp.fetchproductdata.api.response.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/products")
public interface ProductApi {

  @GetMapping("/")
  List<Product> getAllProducts();

  @GetMapping("/categories/{categoryId}")
  List<Product> getProductsByCategory(@RequestParam Long categoryId);

  @GetMapping("/{productId}")
  Product getProductById(@RequestParam Long productId);
}
