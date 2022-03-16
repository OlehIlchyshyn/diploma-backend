package com.nulp.fetchproductdata.service.priceprovider;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.parser.foxtrot.product.FoxtrotProductDataParser;
import com.nulp.fetchproductdata.parser.foxtrot.product.model.ProductData;
import com.nulp.fetchproductdata.parser.foxtrot.search.FoxtrotSearchResultsParser;
import com.nulp.fetchproductdata.parser.foxtrot.search.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FoxtrotPriceProvider implements PriceProvider {

  private final FoxtrotProductDataParser foxtrotProductDataParser;
  private final FoxtrotSearchResultsParser foxtrotSearchResultsParser;

  @Override
  public Price getPriceByProductTitle(String title) {
    SearchResult foxtrotSearchResult = foxtrotSearchResultsParser.searchByProductTitle(title);

    if (!foxtrotSearchResult.isSearchSuccessful()) {
      return null;
    }

    ProductData foxtrotProductData =
        foxtrotProductDataParser.parseDataFromProductPage(foxtrotSearchResult.getUrl());

    if (foxtrotProductData == null) {
      return null;
    }

    return Price.builder()
        .priceProvider(getPriceProvider())
        .amount(foxtrotProductData.getOffers().getPrice())
        .purchaseUrl(foxtrotSearchResult.getUrl())
        .currency(Currency.valueOf(foxtrotProductData.getOffers().getPriceCurrency()))
        .availabilityStatus(Status.getStatus(foxtrotProductData.getOffers().getAvailability()))
        .build();
  }

  public static com.nulp.fetchproductdata.model.PriceProvider getPriceProvider() {
    return com.nulp.fetchproductdata.model.PriceProvider.builder()
        .name("Foxtrot")
        .url("https://www.foxtrot.com.ua/uk")
        .build();
  }
}
