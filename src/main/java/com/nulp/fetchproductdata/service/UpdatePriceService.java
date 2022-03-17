package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.common.PriceUtils;
import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.model.Product;
import com.nulp.fetchproductdata.parser.rozetka.products.details.RozetkaProductDetailsParser;
import com.nulp.fetchproductdata.parser.rozetka.products.details.model.ProductDetails;
import com.nulp.fetchproductdata.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatePriceService {

  private final ProductRepository productRepository;
  private final PriceService priceService;
  private final PriceHistoryService priceHistoryService;
  private final IdMapperService idMapperService;
  private final RozetkaProductDetailsParser rozetkaProductDetailsParser;
  private final PriceUtils priceUtils;

  @Transactional
  public Product updatePrices(long productId) {
    Product product = productRepository.getById(productId);

    List<Price> updatedPrices = priceService.getPriceByProductTitle(product.getFullName());
    Price rozetkaPrice = getRozetkaPrice(product.getFullName());

    List<Price> joinedPriceList = priceUtils.getJoinedList(updatedPrices, rozetkaPrice);
    product.setPriceList(joinedPriceList);

    priceHistoryService.addEntryToPriceHistory(product.getId(), joinedPriceList);

    return product;
  }

  private Price getRozetkaPrice(String title) {
    int rozetkaId = idMapperService.getRozetkaProductIdByTitle(title);
    List<ProductDetails> details =
        rozetkaProductDetailsParser.getProductDetailsByProductId(
            Collections.singletonList(rozetkaId));
    if (details.isEmpty()) {
      return null;
    } else {
      return RozetkaProductListService.getPrice(details.get(0));
    }
  }
}
