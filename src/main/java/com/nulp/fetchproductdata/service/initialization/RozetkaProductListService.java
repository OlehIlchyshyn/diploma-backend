package com.nulp.fetchproductdata.service.initialization;

import com.google.common.collect.Iterables;
import com.nulp.fetchproductdata.common.PriceUtils;
import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.common.enumeration.Status;
import com.nulp.fetchproductdata.config.Properties;
import com.nulp.fetchproductdata.mapping.IdMapperService;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.model.Product;
import com.nulp.fetchproductdata.parser.rozetka.products.details.RozetkaProductDetailsParser;
import com.nulp.fetchproductdata.parser.rozetka.products.details.model.ProductDetails;
import com.nulp.fetchproductdata.parser.rozetka.products.ids.RozetkaProductIdsParser;
import com.nulp.fetchproductdata.repository.ProductRepository;
import com.nulp.fetchproductdata.repository.ProviderRepository;
import com.nulp.fetchproductdata.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
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
  private final ProviderRepository providerRepository;
  private final ProductRepository productRepository;
  private final PriceUtils priceUtils;
  private final Properties properties;

  private final int BUFFER_SIZE = 1000;

  public Set<Product> getProductsByCategoryId(long categoryId) {
    List<Integer> productIds = productIdsParser.getProductIdsByCategory(categoryId);

    if (productIds.isEmpty()) {
      return Collections.emptySet();
    }

    if (properties.getProductPerSubCategoryLimit() != null) {
      if (properties.getProductPerSubCategoryLimit() <= productIds.size()) {
        productIds = productIds.subList(0, properties.getProductPerSubCategoryLimit());
      }
    }

    List<ProductDetails> productDetailsList =
        StreamSupport.stream(Iterables.partition(productIds, BUFFER_SIZE).spliterator(), false)
            .map(productDetailsParser::getProductDetailsByProductId)
            .flatMap(List::stream)
            .collect(Collectors.toList());

    productDetailsList.forEach(
        product -> idMapperService.addRozetkaProductEntry(product.getTitle(), product.getId()));

    Set<Product> products =
        productDetailsList.stream()
            .map(
                productDetails -> {
                  if (idMapperService.getRozetkaProductIdByTitle(productDetails.getTitle())
                      == productDetails.getId()) {
                    Product existingProductEntry =
                        productRepository.findProductByFullName(productDetails.getTitle());
                    if (existingProductEntry != null) {
                      return existingProductEntry;
                    }
                  }
                  return Product.builder()
                      .fullName(productDetails.getTitle())
                      .description(productDetails.getDescription())
                      .imageUrl(productDetails.getMainImageLink())
                      .priceList(
                          priceUtils.getJoinedList(
                              priceService.getPriceByProductTitle(productDetails.getTitle()),
                              getPrice(productDetails)))
                      .techSpecs(
                          techCharacteristicsService.getTechCharacteristicsByProductId(
                              productDetails.getId()))
                      .build();
                })
            .collect(Collectors.toSet());

    productRepository.saveAll(products);
    return products;
  }

  public Price getPrice(ProductDetails rozetkaProductDetails) {
    return Price.builder()
        .amount(rozetkaProductDetails.getPrice())
        .currency(Currency.UAH)
        .availabilityStatus(Status.getStatus(rozetkaProductDetails.getSellStatus()))
        .purchaseUrl(rozetkaProductDetails.getHref().substring(0, Math.min(rozetkaProductDetails.getHref().length(), 255)))
        .priceProvider(getRozetkaPriceProvider())
        .build();
  }

  public com.nulp.fetchproductdata.model.PriceProvider getRozetkaPriceProvider() {
    return providerRepository.getPriceProviderByName("Rozetka");
  }

  public static com.nulp.fetchproductdata.model.PriceProvider priceProvider() {
    return com.nulp.fetchproductdata.model.PriceProvider.builder()
        .name("Rozetka")
        .url("https://rozetka.com.ua/ua/")
        .logoUrl("https://xl-static.rozetka.com.ua/assets/img/main/rozetka.png")
        .build();
  }
}
