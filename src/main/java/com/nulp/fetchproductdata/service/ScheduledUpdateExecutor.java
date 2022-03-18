package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledUpdateExecutor {

  private final UpdatePriceService updatePriceService;
  private final ProductRepository productRepository;

  @Scheduled(cron = "0 0 12 * * ?")
  public void updateALlPricesAt12PM() {
    List<Long> productIds = productRepository.findAllIds();
    productIds.forEach(updatePriceService::updatePrices);
  }
}
