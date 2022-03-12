package com.nulp.fetchproductdata.repository;

import com.nulp.fetchproductdata.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query(
      value =
          "SELECT c.id, c.title FROM category c WHERE c.id NOT IN (SELECT sub_categories_id FROM category_sub_categories)",
      nativeQuery = true)
  List<Category> findAllParentCategories();
}
