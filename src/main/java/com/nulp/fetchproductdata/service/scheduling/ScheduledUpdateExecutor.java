package com.nulp.fetchproductdata.service.scheduling;

import com.nulp.fetchproductdata.repository.ProductRepository;
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

  @Scheduled(cron = "0 0 12 * * ?")
  public void updateALlPricesAt12PM() {
    log.info("Started updating product prices");
    List<Long> productIds = productRepository.findAllIds();
    productIds.forEach(updatePriceService::updatePrices);
    log.info("Product prices updating was finished");
  }
}
