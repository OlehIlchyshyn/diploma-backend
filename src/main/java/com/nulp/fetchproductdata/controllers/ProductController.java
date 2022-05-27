package com.nulp.fetchproductdata.controllers;

import com.nulp.fetchproductdata.api.ProductApi;
import com.nulp.fetchproductdata.api.response.Product;
import com.nulp.fetchproductdata.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ProductController implements ProductApi {

  private final ProductService productService;

  @Override
  public Page<Product> getAllProducts(Long categoryId, Pageable pageable) {
    if (categoryId == null) {
      return productService.getAllProducts(pageable);
    }
    return productService.getProductsByCategory(categoryId, pageable);
  }

  @Override
  public Product getProductById(Long productId) {
    return productService.getProductById(productId);
  }

  @Override
  public Set<Product> findProductsByTitle(String query) {
    return productService.findProductsByTitle(query);
  }
}
