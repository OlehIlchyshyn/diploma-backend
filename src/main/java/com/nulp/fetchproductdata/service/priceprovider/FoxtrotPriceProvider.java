package com.nulp.fetchproductdata.service.priceprovider;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.parser.foxtrot.product.FoxtrotProductDataParser;
import com.nulp.fetchproductdata.parser.foxtrot.product.model.ProductData;
import com.nulp.fetchproductdata.parser.foxtrot.search.FoxtrotSearchResultsParser;
import com.nulp.fetchproductdata.parser.foxtrot.search.model.SearchResult;
import com.nulp.fetchproductdata.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FoxtrotPriceProvider implements PriceProvider {

  private final FoxtrotProductDataParser foxtrotProductDataParser;
  private final FoxtrotSearchResultsParser foxtrotSearchResultsParser;
  private final ProviderRepository providerRepository;

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
        .purchaseUrl(foxtrotSearchResult.getUrl().substring(0, Math.min(foxtrotSearchResult.getUrl().length(), 255)))
        .currency(Currency.valueOf(foxtrotProductData.getOffers().getPriceCurrency()))
        .availabilityStatus(Status.getStatus(foxtrotProductData.getOffers().getAvailability()))
        .build();
  }

  private com.nulp.fetchproductdata.model.PriceProvider getPriceProvider() {
    return providerRepository.getPriceProviderByName("Foxtrot");
  }

  public static com.nulp.fetchproductdata.model.PriceProvider priceProvider() {
    return com.nulp.fetchproductdata.model.PriceProvider.builder()
        .name("Foxtrot")
        .url("https://www.foxtrot.com.ua/uk")
        .logoUrl("https://macaroncms.s3.eu-central-1.amazonaws.com/UploadsCatalog/foxtrot.png")
        .build();
  }
}
