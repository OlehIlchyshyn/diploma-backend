package com.nulp.fetchproductdata.service;

import com.google.common.collect.Iterables;
import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.model.Product;
import com.nulp.fetchproductdata.parser.rozetka.products.details.RozetkaProductDetailsParser;
import com.nulp.fetchproductdata.parser.rozetka.products.details.model.ProductDetails;
import com.nulp.fetchproductdata.parser.rozetka.products.ids.RozetkaProductIdsParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProductListService {

  private final RozetkaProductIdsParser productIdsParser;
  private final RozetkaProductDetailsParser productDetailsParser;
  private final PriceService priceService;
  private final TechCharacteristicsService techCharacteristicsService;
  private final int bufferSize = 1000;
  private final int OUTLIERS_PERCENTAGE = 50;

  public List<Product> getProductsByCategoryId(long categoryId) {
    List<Integer> productIds = productIdsParser.getProductIdsByCategory(categoryId);
    // TODO: remove sublist
    productIds = productIds.subList(0, 3);
    List<ProductDetails> productDetailsList =
        StreamSupport.stream(Iterables.partition(productIds, bufferSize).spliterator(), false)
            .map(productDetailsParser::getProductDetailsByProductId)
            .flatMap(List::stream)
            .collect(Collectors.toList());

    return productDetailsList.stream()
        .map(
            productDetails ->
                Product.builder()
                    .fullName(productDetails.getTitle())
                    .description(productDetails.getDescription())
                    .priceList(
                        getJoinedList(
                            priceService.getPriceByProductTitle(productDetails.getTitle()),
                            getPrice(productDetails)))
                    .techSpecs(
                        techCharacteristicsService.getTechCharacteristicsByProductId(
                            productDetails.getId()))
                    .build())
        .collect(Collectors.toList());
  }

  private List<Price> getJoinedList(List<Price> priceList, Price rozetkaPrice) {
    List<Price> priceListWithoutOutliers = removeOutOfRangePrices(priceList, rozetkaPrice);
    priceListWithoutOutliers.add(rozetkaPrice);
    return priceListWithoutOutliers;
  }

  private List<Price> removeOutOfRangePrices(List<Price> priceList, Price rozetkaPrice) {
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

  private Price getPrice(ProductDetails rozetkaProductDetails) {
    return Price.builder()
        .amount(rozetkaProductDetails.getPrice())
        .currency(Currency.UAH)
        .availabilityStatus(Status.getStatus(rozetkaProductDetails.getSellStatus()))
        .purchaseUrl(rozetkaProductDetails.getHref())
        .priceProvider(getRozetkaPriceProvider())
        .build();
  }

  public static com.nulp.fetchproductdata.model.PriceProvider getRozetkaPriceProvider() {
    return com.nulp.fetchproductdata.model.PriceProvider.builder()
        .name("Rozetka")
        .url("https://rozetka.com.ua/ua/")
        .build();
  }
}
