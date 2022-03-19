package com.nulp.fetchproductdata.common;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.config.Properties;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.service.ConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PriceUtils {

  private final ConversionService conversionService;
  private final Properties properties;

  public final int OUTLIERS_PERCENTAGE = 50;

  public List<Price> getJoinedList(List<Price> priceList, Price rozetkaPrice) {
    List<Price> priceListWithoutOutliers = removeOutOfRangePrices(priceList, rozetkaPrice);
    priceListWithoutOutliers.add(rozetkaPrice);
    return priceListWithoutOutliers;
  }

  public List<Price> removeOutOfRangePrices(List<Price> priceList, Price rozetkaPrice) {
    Currency desiredCurrency = properties.getPreferredCurrency();
    if (rozetkaPrice.getCurrency() != properties.getPreferredCurrency()) {
      rozetkaPrice.setAmount(
          conversionService.convert(
              rozetkaPrice.getCurrency(), desiredCurrency, rozetkaPrice.getAmount()));
      rozetkaPrice.setCurrency(desiredCurrency);
    }
    return priceList.stream()
        .filter(
            price -> {
              if (price.getCurrency() != properties.getPreferredCurrency()) {
                price.setAmount(
                    conversionService.convert(
                        price.getCurrency(), desiredCurrency, price.getAmount()));
                price.setCurrency(desiredCurrency);
              }
              if (price.getAmount() > rozetkaPrice.getAmount()) {
                return ((price.getAmount() / rozetkaPrice.getAmount() * 100) - 100)
                    <= OUTLIERS_PERCENTAGE;
              } else {
                return ((rozetkaPrice.getAmount() / price.getAmount() * 100) - 100)
                    <= OUTLIERS_PERCENTAGE;
              }
            })
        .collect(Collectors.toList());
  }
}
