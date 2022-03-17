package com.nulp.fetchproductdata.controllers;

import com.nulp.fetchproductdata.api.ProductApi;
import com.nulp.fetchproductdata.api.response.Product;
import com.nulp.fetchproductdata.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

  private final ProductService productService;

  @Override
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @Override
  public List<Product> getProductsByCategory(Long categoryId) {
    return productService.getProductsByCategory(categoryId);
  }

  @Override
  public Product getProductById(Long productId) {
    return productService.getProductById(productId);
  }
}