package com.nulp.fetchproductdata.common;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.config.Properties;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.service.ConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PriceUtils {

  private final ConversionService conversionService;
  private final Properties properties;

  public List<Price> getJoinedList(List<Price> priceList, Price rozetkaPrice) {
    if (properties.isFilteringEnabled()) {
      List<Price> priceListWithoutOutliers = removeOutOfRangePrices(priceList, rozetkaPrice);
      priceListWithoutOutliers.add(rozetkaPrice);
      return priceListWithoutOutliers;
    }
    List<Price> unfilteredList = new ArrayList<>(priceList);
    unfilteredList.add(rozetkaPrice);
    return unfilteredList;
  }

  public List<Price> removeOutOfRangePrices(List<Price> priceList, Price rozetkaPrice) {
    Currency desiredCurrency = properties.getPreferredCurrency();
    convertToDesiredCurrency(rozetkaPrice, desiredCurrency);
    return priceList.stream()
        .filter(
            price -> {
              convertToDesiredCurrency(price, desiredCurrency);
              return isPriceNotTooDifferentFromRozetka(price, rozetkaPrice);
            })
        .collect(Collectors.toList());
  }

  private boolean isPriceNotTooDifferentFromRozetka(Price price, Price rozetkaPrice) {
    if (price.getAmount() > rozetkaPrice.getAmount()) {
      return ((price.getAmount() / rozetkaPrice.getAmount() * 100) - 100)
          <= properties.getOutliersPercentage();
    } else {
      return ((rozetkaPrice.getAmount() / price.getAmount() * 100) - 100)
          <= properties.getOutliersPercentage();
    }
  }

  private void convertToDesiredCurrency(Price price, Currency desiredCurrency) {
    if (price.getCurrency() != properties.getPreferredCurrency()) {
      price.setAmount(
          conversionService.convert(price.getCurrency(), desiredCurrency, price.getAmount()));
      price.setCurrency(desiredCurrency);
    }
  }
}
