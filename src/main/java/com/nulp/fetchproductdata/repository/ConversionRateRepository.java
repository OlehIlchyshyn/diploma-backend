package com.nulp.fetchproductdata.repository;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.model.ConversionRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversionRateRepository extends JpaRepository<ConversionRate, Long> {

  ConversionRate findConversionRateByFromCurrencyAndToCurrency(Currency from, Currency to);
}
