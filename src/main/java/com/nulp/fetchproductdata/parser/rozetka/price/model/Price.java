package com.nulp.fetchproductdata.parser.rozetka.price.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Price {

  @SerializedName("old_price")
  private final double oldPrice;

  @SerializedName("old_usd_price")
  private final double oldUsdPrice;

  private final double price;

  @SerializedName("usd_price")
  private final double usdPrice;

  private final String status;

  @SerializedName("sell_status")
  private final String sellStatus;
}
