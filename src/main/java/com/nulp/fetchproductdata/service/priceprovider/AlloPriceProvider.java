package com.nulp.fetchproductdata.service.priceprovider;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.parser.allo.product.AlloProductDataParser;
import com.nulp.fetchproductdata.parser.allo.product.model.ProductData;
import com.nulp.fetchproductdata.parser.allo.search.AlloSearchResultParser;
import com.nulp.fetchproductdata.parser.foxtrot.search.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
@RequiredArgsConstructor
public class AlloPriceProvider implements PriceProvider {

  private final AlloSearchResultParser alloSearchResultParser;
  private final AlloProductDataParser alloProductDataParser;

  @Nullable
  @Override
  public Price getPriceByProductTitle(String title) {
    SearchResult searchResult = alloSearchResultParser.searchByProductTitle(title);

    if (!searchResult.isSearchSuccessful()) {
      return null;
    }

    ProductData productData = alloProductDataParser.parseDataFromProductPage(searchResult.getUrl());
    if (productData == null) {
      return null;
    }

    return Price.builder()
        .priceProvider(getPriceProvider())
        .amount(productData.getPrice())
        .purchaseUrl(searchResult.getUrl())
        .currency(Currency.valueOf(productData.getCurrency()))
        .availabilityStatus(Status.getStatus(productData.getAvailability()))
        .build();
  }

  public static com.nulp.fetchproductdata.model.PriceProvider getPriceProvider() {
    return com.nulp.fetchproductdata.model.PriceProvider.builder()
        .name("Allo")
        .url("https://allo.ua/")
        .build();
  }
}
