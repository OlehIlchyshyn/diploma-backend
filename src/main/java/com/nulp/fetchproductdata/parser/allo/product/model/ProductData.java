package com.nulp.fetchproductdata.parser.allo.product.model;

import lombok.Data;

@Data
public class ProductData {

  private final String sku;

  private final String title;

  private final double oldPrice;

  private final String discount;

  private final double price;

  private final String currency;

  private final String availability;

  private final String priceValidUntil;
}
