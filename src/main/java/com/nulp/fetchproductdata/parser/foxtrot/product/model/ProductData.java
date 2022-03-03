package com.nulp.fetchproductdata.parser.foxtrot.product.model;

import lombok.Data;

import java.util.List;

@Data
public class ProductData {

    private final long sku;

    private final String name;

   private final Offer offers;

    private final List<String> image;
}
