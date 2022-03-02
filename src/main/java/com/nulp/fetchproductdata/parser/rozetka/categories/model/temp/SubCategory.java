package com.nulp.fetchproductdata.parser.rozetka.categories.model.temp;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class SubCategory {
    private final String title;
    @SerializedName("category_id")
    private final long categoryId;
    @SerializedName("top_category_id")
    private final long topCategoryId;
    @SerializedName("manual_url")
    private String manualUrl;
    private List<SubCategory> children;
}
