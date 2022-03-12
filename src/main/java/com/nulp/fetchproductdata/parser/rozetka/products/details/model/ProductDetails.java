package com.nulp.fetchproductdata.parser.rozetka.products.details.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ProductDetails {

  private final int id;

  private final String title;

  private final double price;

  @SerializedName("old_price")
  private final double oldPrice;

  private final String status;

  @SerializedName("sell_status")
  private final String sellStatus;

  private final String brand;

  private final String href;

  private final String state;

  @SerializedName("docket")
  private final String description;

  @SerializedName("image_main")
  private final String mainImageLink;
}
