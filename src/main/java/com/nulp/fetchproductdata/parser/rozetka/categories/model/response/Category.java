package com.nulp.fetchproductdata.parser.rozetka.categories.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Category {
    private String title;
    private int categoryId;
    private int topCategoryId;
    private String manualUrl;
    private List<Category> children;
}