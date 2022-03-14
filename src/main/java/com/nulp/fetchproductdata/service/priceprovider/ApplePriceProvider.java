package com.nulp.fetchproductdata.service.priceprovider;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.parser.apple.price.AppleProductPriceParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;


@Component
@RequiredArgsConstructor
public class ApplePriceProvider implements PriceProvider {

    private final AppleProductPriceParser appleProductPriceParser;

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
        return title.length() == 9 && title.contains("UA/A");
    }

    private String getSkuFromTitle(String title) {
        return title.replaceAll("UA/A", "");
    }
}
