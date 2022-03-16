package com.nulp.fetchproductdata.common;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.model.Price;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceUtils {
    public final int OUTLIERS_PERCENTAGE = 50;

    public List<Price> getJoinedList(List<Price> priceList, Price rozetkaPrice) {
        List<Price> priceListWithoutOutliers = removeOutOfRangePrices(priceList, rozetkaPrice);
        priceListWithoutOutliers.add(rozetkaPrice);
        return priceListWithoutOutliers;
    }

    public List<Price> removeOutOfRangePrices(List<Price> priceList, Price rozetkaPrice) {
        return priceList.stream()
                .filter(
                        price -> {
                            // todo make currency conversion and check whether the value is outlier AFTER this
                            // conversion
                            if (price.getCurrency() == Currency.USD || price.getCurrency() == Currency.EUR) {
                                return true;
                            } else {
                                if (price.getAmount() > rozetkaPrice.getAmount()) {
                                    return ((price.getAmount() / rozetkaPrice.getAmount() * 100) - 100)
                                            <= OUTLIERS_PERCENTAGE;
                                } else {
                                    return ((rozetkaPrice.getAmount() / price.getAmount() * 100) - 100)
                                            <= OUTLIERS_PERCENTAGE;
                                }
                            }
                        })
                .collect(Collectors.toList());
    }
}