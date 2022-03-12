package com.nulp.fetchproductdata.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Category {

  private long id;

  private String title;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<Category> subCategories;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<Product> productList;
}
