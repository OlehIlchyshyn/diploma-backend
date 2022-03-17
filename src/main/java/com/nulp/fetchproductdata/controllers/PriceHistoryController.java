package com.nulp.fetchproductdata.controllers;

import com.nulp.fetchproductdata.api.PriceHistoryApi;
import com.nulp.fetchproductdata.api.response.PriceHistory;
import com.nulp.fetchproductdata.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PriceHistoryController implements PriceHistoryApi {

  private final PriceHistoryService priceHistoryService;

  @Override
  public PriceHistory getHistoryByProduct(Long productId) {
    return priceHistoryService.getPriceHistoryForProduct(productId);
  }
}
