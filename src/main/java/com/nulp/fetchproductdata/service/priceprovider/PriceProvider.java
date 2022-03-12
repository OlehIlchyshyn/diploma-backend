package com.nulp.fetchproductdata.service.priceprovider;

import com.nulp.fetchproductdata.model.Price;

public interface PriceProvider {
  Price getPriceByProductTitle(String title);
}
