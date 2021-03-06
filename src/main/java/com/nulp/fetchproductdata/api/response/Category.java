package com.nulp.fetchproductdata.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class Category {

  private long id;

  private String title;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Set<Category> subCategories;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Set<Product> productList;
}
