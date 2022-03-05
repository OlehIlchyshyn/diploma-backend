package com.nulp.fetchproductdata.api.response;

import lombok.Data;

import java.util.List;

@Data
public class Catalog {
    private final List<Category> categoryList;
}
