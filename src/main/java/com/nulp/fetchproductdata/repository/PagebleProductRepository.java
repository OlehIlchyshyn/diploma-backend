package com.nulp.fetchproductdata.repository;

import com.nulp.fetchproductdata.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagebleProductRepository extends PagingAndSortingRepository<Product, Long> {
  @Query("SELECT p FROM Product p JOiN p.categories c WHERE c.id=:categoryId")
  Page<Product> findProductsByCategoryId(Long categoryId, Pageable pageable);
}
