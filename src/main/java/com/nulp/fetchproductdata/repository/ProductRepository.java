package com.nulp.fetchproductdata.repository;

import com.nulp.fetchproductdata.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT c.products FROM Category c WHERE c.id=:categoryId")
  List<Product> findProductsByCategoryId(Long categoryId);

  @Query("SELECT p.id FROM Product p")
  List<Long> findAllIds();
}
