package com.nulp.fetchproductdata.parser.rozetka.categories.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Category {
    private String title;
    private long categoryId;
    private long topCategoryId;
    private String manualUrl;
    private List<Category> children;
}