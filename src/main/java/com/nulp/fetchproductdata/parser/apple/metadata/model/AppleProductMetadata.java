package com.nulp.fetchproductdata.parser.apple.metadata.model;

import lombok.Data;

@Data
public class AppleProductMetadata {
  private final String name;
  private final PriceMetadata price;
  private final String sku;
  private final String category;
}
