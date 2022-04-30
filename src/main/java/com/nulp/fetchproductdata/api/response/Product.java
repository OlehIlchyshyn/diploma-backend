package com.nulp.fetchproductdata.api.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Product {

  private long id;

  private String fullName;

  private String description;

  private Map<String, Map<String, String>> techSpecs;

  private Double averagePrice;

  private List<Price> priceList;

  private String imageUrl;
}
