package com.nulp.fetchproductdata.parser.rozetka.products.ids.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class IdsResponse {

  private final List<Integer> ids;

  @SerializedName("ids_count")
  private final int count;

  @SerializedName("total_pages")
  private final int pagesCount;
}
