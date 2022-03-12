package com.nulp.fetchproductdata.parser.rozetka.categories.model.temp;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RootCategory {
  private final String title;

  @SerializedName("category_id")
  private final int categoryId;

  @SerializedName("top_category_id")
  private final int topCategoryId;

  @SerializedName("manual_url")
  private final String manualUrl;

  private ChildrenObject children;
}
