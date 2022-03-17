package com.nulp.fetchproductdata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @OneToOne(fetch = FetchType.EAGER)
  private Product product;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<PriceRecord> priceRecords;

  public void addPriceRecord(PriceRecord priceRecord) {
    if (priceRecords == null) {
      priceRecords = new LinkedList<>();
    }
    priceRecords.add(priceRecord);
  }
}
