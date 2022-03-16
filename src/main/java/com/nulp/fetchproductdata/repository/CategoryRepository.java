package com.nulp.fetchproductdata.repository;

import com.nulp.fetchproductdata.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query(value = "SELECT c FROM Category c LEFT JOIN FETCH c.parent WHERE c.parent IS NULL")
  Set<Category> findAllParentCategories();

  @Query(
      value =
          "SELECT c FROM Category c JOIN FETCH c.subCategories LEFT JOIN FETCH c.parent WHERE c.parent IS NULL")
  Set<Category> findAllParentCategoriesWithSubcategories();
}
