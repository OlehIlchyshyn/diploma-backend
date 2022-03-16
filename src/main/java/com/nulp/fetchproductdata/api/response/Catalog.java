package com.nulp.fetchproductdata.api.response;

import lombok.Data;

import java.util.Set;

@Data
public class Catalog {
  private final Set<Category> categoryList;
}
