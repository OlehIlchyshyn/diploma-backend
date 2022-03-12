package com.nulp.fetchproductdata.service.priceprovider;

import com.nulp.fetchproductdata.model.Price;

import javax.annotation.Nullable;

public interface PriceProvider {
  @Nullable
  Price getPriceByProductTitle(String title);
}
