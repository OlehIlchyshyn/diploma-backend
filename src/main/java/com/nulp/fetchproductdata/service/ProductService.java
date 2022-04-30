package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.api.response.Product;
import com.nulp.fetchproductdata.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductSearchService productSearchService;
  private final ModelMapper modelMapper;

  public Product getProductById(Long productId) {
    Optional<com.nulp.fetchproductdata.model.Product> productOptional =
        productRepository.findProductById(productId);
    if (productOptional.isEmpty()) {
      return null;
    } else {
      return mapToResponse(productOptional.get());
    }
  }

  @Transactional
  public Set<Product> getProductsByCategory(Long categoryId) {
    return productRepository.findProductsByCategoryId(categoryId).stream()
        .map(this::mapToResponse)
        .collect(Collectors.toSet());
  }

  @Transactional
  public Set<Product> getAllProducts() {
    return productRepository.findAll().stream()
        .map(this::mapToResponse)
        .collect(Collectors.toSet());
  }

  public Set<Product> findProductsByTitle(String title) {
    return productSearchService.findProductsByTitle(title).stream()
        .map(this::mapToResponse)
        .collect(Collectors.toSet());
  }

  private Product mapToResponse(com.nulp.fetchproductdata.model.Product productModel) {
    return modelMapper.map(productModel, Product.class);
  }

  private Set<Product> mapToResponse(
      Set<com.nulp.fetchproductdata.model.Product> productModelList) {
    return productModelList.stream()
        .map(
            productModel -> {
              Product product = new Product();
              product.setId(productModel.getId());
              product.setFullName(productModel.getFullName());
              product.setDescription(productModel.getDescription());
              return product;
            })
        .collect(Collectors.toSet());
  }
}
