package com.nulp.fetchproductdata.service.initialization;

import com.nulp.fetchproductdata.repository.ProviderRepository;
import com.nulp.fetchproductdata.service.priceprovider.AlloPriceProvider;
import com.nulp.fetchproductdata.service.priceprovider.ApplePriceProvider;
import com.nulp.fetchproductdata.service.priceprovider.FoxtrotPriceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InitPriceProvidersService {

  private final ProviderRepository providerRepository;

  public void initProviders() {
    providerRepository.deleteAll();
    providerRepository.saveAll(
        List.of(
            ApplePriceProvider.priceProvider(),
            FoxtrotPriceProvider.priceProvider(),
            AlloPriceProvider.priceProvider(),
            RozetkaProductListService.priceProvider()));
  }
}
