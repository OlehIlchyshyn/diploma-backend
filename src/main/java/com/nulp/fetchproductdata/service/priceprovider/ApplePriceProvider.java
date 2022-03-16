package com.nulp.fetchproductdata.service.priceprovider;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.parser.apple.price.AppleProductPriceParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplePriceProvider implements PriceProvider {

  private final AppleProductPriceParser appleProductPriceParser;

  private final List<String> possibleAppleSkuEnding =
      List.of("UA/A", "ZM/A", "RS/A", "UL/A", "TY/A", "RU/A", "ZE/A", "RK/A");

  @Nullable
  @Override
  public Price getPriceByProductTitle(String title) {
    if (isAppleProductSku(title)) {
      double price = appleProductPriceParser.getPriceBySku(getSkuFromTitle(title));
      return Price.builder()
          .purchaseUrl("https://www.apple.com/store")
          .priceProvider(getPriceProvider())
          .availabilityStatus(Status.AVAILABLE)
          .amount(price)
          .currency(Currency.USD)
          .build();
    }
    return null;
  }

  public static com.nulp.fetchproductdata.model.PriceProvider getPriceProvider() {
    return com.nulp.fetchproductdata.model.PriceProvider.builder()
        .name("Apple")
        .url("https://www.apple.com/")
        .build();
  }

  private boolean isAppleProductSku(String title) {
    return title.length() == 9 && possibleAppleSkuEnding.stream().anyMatch(title::contains);
  }

  private String getSkuFromTitle(String title) {
    for (String ending : possibleAppleSkuEnding) {
      title = title.replaceAll(ending, "");
    }
    return title;
  }
}
