package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.common.PriceUtils;
import com.nulp.fetchproductdata.mapping.IdMapperService;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.model.Product;
import com.nulp.fetchproductdata.parser.rozetka.products.details.RozetkaProductDetailsParser;
import com.nulp.fetchproductdata.parser.rozetka.products.details.model.ProductDetails;
import com.nulp.fetchproductdata.repository.ProductRepository;
import com.nulp.fetchproductdata.service.initialization.RozetkaProductListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePriceService {

  private final ProductRepository productRepository;
  private final PriceService priceService;
  private final PriceHistoryService priceHistoryService;
  private final IdMapperService idMapperService;
  private final RozetkaProductDetailsParser rozetkaProductDetailsParser;
  private final RozetkaProductListService rozetkaProductListService;
  private final PriceUtils priceUtils;

  public void updatePrices(long productId) {
    Product product = productRepository.findProductById(productId).orElse(null);
    if (product == null) {
      log.warn("Product with given id:" + productId + " doesn't exists");
      return;
    }

    List<Price> updatedPrices = priceService.getPriceByProductTitle(product.getFullName());
    Price rozetkaPrice = getRozetkaPrice(product.getFullName());

    List<Price> joinedPriceList = priceUtils.getJoinedList(updatedPrices, rozetkaPrice);
    product.setPriceList(joinedPriceList);

    priceHistoryService.addEntryToPriceHistory(product.getId(), joinedPriceList);
  }

  private Price getRozetkaPrice(String title) {
    int rozetkaId;
    try {
      rozetkaId = idMapperService.getRozetkaProductIdByTitle(title);
    } catch (NoSuchElementException exception) {
      log.warn("No rozetka id was found for product title: " + title);
      return null;
    }
    List<ProductDetails> details =
        rozetkaProductDetailsParser.getProductDetailsByProductId(
            Collections.singletonList(rozetkaId));
    if (details.isEmpty()) {
      return null;
    } else {
      return rozetkaProductListService.getPrice(details.get(0));
    }
  }
}
