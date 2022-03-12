package com.nulp.fetchproductdata.parser.rozetka.categories.model.temp;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class SubCategory {
    private final String title;
    @SerializedName("category_id")
    private final int categoryId;
    @SerializedName("top_category_id")
    private final int topCategoryId;
    @SerializedName("manual_url")
    private String manualUrl;
    private List<SubCategory> children;
}
