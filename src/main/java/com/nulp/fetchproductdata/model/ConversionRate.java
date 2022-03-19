package com.nulp.fetchproductdata.model;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionRate {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Enumerated(EnumType.STRING)
  private Currency fromCurrency;

  @Enumerated(EnumType.STRING)
  private Currency toCurrency;

  private double rate;
}
