package com.nulp.fetchproductdata.api;

import com.nulp.fetchproductdata.api.response.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/categories")
public interface CategoryApi {

  @GetMapping("/{categoryId}")
  Category getCategoryById(@PathVariable Long categoryId);
}
