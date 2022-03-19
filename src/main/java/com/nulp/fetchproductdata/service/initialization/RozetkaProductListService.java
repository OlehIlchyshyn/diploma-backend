package com.nulp.fetchproductdata.service.initialization;

import com.google.common.collect.Iterables;
import com.nulp.fetchproductdata.common.PriceUtils;
import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
import com.nulp.fetchproductdata.config.Properties;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.model.Product;
import com.nulp.fetchproductdata.parser.rozetka.products.details.RozetkaProductDetailsParser;
import com.nulp.fetchproductdata.parser.rozetka.products.details.model.ProductDetails;
import com.nulp.fetchproductdata.parser.rozetka.products.ids.RozetkaProductIdsParser;
import com.nulp.fetchproductdata.service.IdMapperService;
import com.nulp.fetchproductdata.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RozetkaProductListService {

  private final RozetkaProductIdsParser productIdsParser;
  private final RozetkaProductDetailsParser productDetailsParser;
  private final PriceService priceService;
  private final TechCharacteristicsService techCharacteristicsService;
  private final IdMapperService idMapperService;
  private final PriceUtils priceUtils;
  private final Properties properties;

  private final int BUFFER_SIZE = 1000;

  public List<Product> getProductsByCategoryId(long categoryId) {
    List<Integer> productIds = productIdsParser.getProductIdsByCategory(categoryId);

    if (properties.getProductPerSubCategoryLimit() != null) {
      productIds = productIds.subList(0, properties.getProductPerSubCategoryLimit());
    }

    List<ProductDetails> productDetailsList =
        StreamSupport.stream(Iterables.partition(productIds, BUFFER_SIZE).spliterator(), false)
            .map(productDetailsParser::getProductDetailsByProductId)
            .flatMap(List::stream)
            .collect(Collectors.toList());

    productDetailsList.forEach(
        product -> idMapperService.addRozetkaProductEntry(product.getTitle(), product.getId()));

    return productDetailsList.stream()
        .map(
            productDetails ->
                Product.builder()
                    .fullName(productDetails.getTitle())
                    .description(productDetails.getDescription())
                    .priceList(
                        priceUtils.getJoinedList(
                            priceService.getPriceByProductTitle(productDetails.getTitle()),
                            getPrice(productDetails)))
                    .techSpecs(
                        techCharacteristicsService.getTechCharacteristicsByProductId(
                            productDetails.getId()))
                    .build())
        .collect(Collectors.toList());
  }

  public static Price getPrice(ProductDetails rozetkaProductDetails) {
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
