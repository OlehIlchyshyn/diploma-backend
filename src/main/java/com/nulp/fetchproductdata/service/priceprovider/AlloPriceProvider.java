package com.nulp.fetchproductdata.service.priceprovider;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.parser.allo.product.AlloProductDataParser;
import com.nulp.fetchproductdata.parser.allo.product.model.ProductData;
import com.nulp.fetchproductdata.parser.allo.search.AlloSearchResultParser;
import com.nulp.fetchproductdata.parser.foxtrot.search.model.SearchResult;
import com.nulp.fetchproductdata.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
@RequiredArgsConstructor
public class AlloPriceProvider implements PriceProvider {

  private final AlloSearchResultParser alloSearchResultParser;
  private final AlloProductDataParser alloProductDataParser;
  private final ProviderRepository providerRepository;

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

  private com.nulp.fetchproductdata.model.PriceProvider getPriceProvider() {
    return providerRepository.getPriceProviderByName("Allo");
  }

  public static com.nulp.fetchproductdata.model.PriceProvider priceProvider() {
    return com.nulp.fetchproductdata.model.PriceProvider.builder()
        .name("Allo")
        .url("https://allo.ua/")
        .logoUrl(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Ukrainian_Allo_logo.svg/2560px-Ukrainian_Allo_logo.svg.png")
        .build();
  }
}
