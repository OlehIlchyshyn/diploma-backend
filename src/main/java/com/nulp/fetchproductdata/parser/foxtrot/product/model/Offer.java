package com.nulp.fetchproductdata.parser.foxtrot.product.model;

import lombok.Data;

@Data
public class Offer {

    private final int price;

    private final String priceCurrency;

    private final String priceValidUntil;

    private final String itemCondition;

    private final String availability;
}
