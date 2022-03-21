package com.nulp.fetchproductdata.service.scheduling;

import com.nulp.fetchproductdata.repository.ConversionRateRepository;
import com.nulp.fetchproductdata.repository.ProductRepository;
import com.nulp.fetchproductdata.service.ConversionService;
import com.nulp.fetchproductdata.service.UpdatePriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledUpdateExecutor {

  private final UpdatePriceService updatePriceService;
  private final ProductRepository productRepository;
  private final ConversionService conversionService;
  private final ConversionRateRepository conversionRateRepository;

  @Scheduled(cron = "0 0 0 * * ?")
  public void updateALlPricesAtMidnight() {
    log.info("Started updating product prices");
    List<Long> productIds = productRepository.findAllIds();
    productIds.forEach(updatePriceService::updatePrices);
    log.info("Product prices updating was finished");
  }

  @Scheduled(cron = "0 0 12 * * ?")
  public void updateAllRatesAtNoon() {
    log.info("Started updating conversion rates");
    conversionRateRepository.deleteAll();
    conversionRateRepository.saveAll(conversionService.getAllRates());
    log.info("Conversion rates updating was finished");
  }
}
