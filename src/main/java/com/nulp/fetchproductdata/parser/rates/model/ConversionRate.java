package com.nulp.fetchproductdata.parser.rates.model;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversionRate {

  private Currency fromCurrency;

  private Currency toCurrency;

  private double rate;
}
