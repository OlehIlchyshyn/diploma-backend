package com.nulp.fetchproductdata.repository;

import com.nulp.fetchproductdata.model.PriceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<PriceProvider, Long> {
  PriceProvider getPriceProviderByName(String name);
}
