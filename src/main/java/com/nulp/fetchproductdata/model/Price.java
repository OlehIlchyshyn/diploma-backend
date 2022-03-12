package com.nulp.fetchproductdata.model;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private Currency currency;

  private double amount;

  private String status;

  private String purchaseUrl;

  @OneToOne(cascade = CascadeType.PERSIST)
  private PriceProvider priceProvider;
}
