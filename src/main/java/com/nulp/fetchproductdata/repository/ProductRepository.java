package com.nulp.fetchproductdata.repository;

import com.nulp.fetchproductdata.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT p FROM Product p JOIN FETCH p.priceList WHERE p.id = :productId")
  Optional<Product> findProductById(Long productId);

  @Query("SELECT c.products FROM Category c WHERE c.id=:categoryId")
  Set<Product> findProductsByCategoryId(Long categoryId);

  @Query("SELECT p.id FROM Product p")
  List<Long> findAllIds();

  @Query("SELECT p FROM Product p JOIN FETCH p.priceList WHERE p.fullName=:title")
  Product findProductByFullName(String title);
}
