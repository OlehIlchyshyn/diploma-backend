package com.nulp.fetchproductdata.common.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private double amount;

    @OneToOne
    private PriceProvider priceProvider;
}
