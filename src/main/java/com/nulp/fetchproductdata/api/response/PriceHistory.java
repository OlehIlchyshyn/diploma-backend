package com.nulp.fetchproductdata.api.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PriceHistory {

  private Long id;

  private Product product;

  private Map<Date, List<Price>> priceRecords;
}
