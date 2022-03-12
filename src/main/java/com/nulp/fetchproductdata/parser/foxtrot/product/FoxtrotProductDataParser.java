package com.nulp.fetchproductdata.parser.foxtrot.product;

import com.google.gson.Gson;
import com.nulp.fetchproductdata.parser.foxtrot.product.model.ProductData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class FoxtrotProductDataParser {

  public ProductData parseDataFromProductPage(String productPageUrl) {
    try {
      Document htmlDocument = Jsoup.connect(productPageUrl).get();
      String productDataJson =
          htmlDocument.getElementsByAttributeValue("name", "product_structured_data").get(0).data();

      return translateJsonToProductData(productDataJson);
    } catch (HttpStatusException e) {
      log.warn("Can't fetch product for url: " + productPageUrl);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private ProductData translateJsonToProductData(String json) {
    return new Gson().fromJson(json, ProductData.class);
  }
}
