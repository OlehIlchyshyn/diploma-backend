package com.nulp.fetchproductdata.api;

import com.nulp.fetchproductdata.api.response.PriceHistory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/priceHistory/")
public interface PriceHistoryApi {

  @GetMapping("/product/{productId}")
  PriceHistory getHistoryByProduct(@PathVariable Long productId);
}
