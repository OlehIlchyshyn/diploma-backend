package com.nulp.fetchproductdata.model;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Enumerated(EnumType.STRING)
  private Currency currency;

  private double amount;

  @Enumerated(EnumType.STRING)
  private Status availabilityStatus;

  private String purchaseUrl;

  @OneToOne(fetch = FetchType.EAGER)
  private PriceProvider priceProvider;
}
