package com.nulp.fetchproductdata.api.response;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
import com.nulp.fetchproductdata.model.PriceProvider;
import lombok.Data;

@Data
public class Price {

  private Long id;

  private Currency currency;

  private double amount;

  private Status availabilityStatus;

  private String purchaseUrl;

  private PriceProvider priceProvider;
}
