package com.nulp.fetchproductdata.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Category {
    private long id;
    private String title;
    private List<Category> subCategories;
}
