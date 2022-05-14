package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.api.response.Price;
import com.nulp.fetchproductdata.api.response.PriceHistory;
import com.nulp.fetchproductdata.model.PriceRecord;
import com.nulp.fetchproductdata.model.Product;
import com.nulp.fetchproductdata.repository.PriceHistoryRepository;
import com.nulp.fetchproductdata.repository.PriceRepository;
import com.nulp.fetchproductdata.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceHistoryService {

  private final PriceHistoryRepository priceHistoryRepository;
  private final ProductRepository productRepository;
  private final PriceRepository priceRepository;
  private final ModelMapper modelMapper;

  @Transactional
  public PriceHistory getPriceHistoryForProduct(long productId) {
    return mapToResponse(priceHistoryRepository.getPriceHistoryByProductId(productId));
  }

  @Transactional
  public void addEntryToPriceHistory(
      long productId, List<com.nulp.fetchproductdata.model.Price> updatedPrices) {
    updatedPrices = priceRepository.saveAllAndFlush(updatedPrices);
    Product product = productRepository.findProductById(productId).orElse(null);

    if (product == null) {
      log.error("Product with id:" + productId + " does not exists");
      return;
    }
    product.setPriceList(updatedPrices);

    PriceRecord newRecord =
        PriceRecord.builder().date(getRecordDate()).priceList(updatedPrices).build();

    com.nulp.fetchproductdata.model.PriceHistory priceHistory =
        priceHistoryRepository.getPriceHistoryByProductId(productId);
    if (priceHistory == null) {
      priceHistory =
          com.nulp.fetchproductdata.model.PriceHistory.builder()
              .product(product)
              .priceRecords(new LinkedList<>(Collections.singletonList(newRecord)))
              .build();
    } else {
      priceHistory.addPriceRecord(newRecord);
    }
    priceHistoryRepository.save(priceHistory);
  }

  private Date getRecordDate() {
    return new Date();
  }

  private PriceHistory mapToResponse(
      com.nulp.fetchproductdata.model.PriceHistory priceHistoryModel) {
    PriceHistory priceHistory = modelMapper.map(priceHistoryModel, PriceHistory.class);
    priceHistory.setPriceRecords(convertPriceRecordToMap(priceHistoryModel.getPriceRecords()));
    return priceHistory;
  }

  private Map<Date, List<Price>> convertPriceRecordToMap(List<PriceRecord> priceRecords) {
    return priceRecords.stream()
        .collect(
            Collectors.toMap(
                PriceRecord::getDate,
                record ->
                    record.getPriceList().stream()
                        .map(priceModel -> modelMapper.map(priceModel, Price.class))
                        .collect(Collectors.toList())));
  }
}
