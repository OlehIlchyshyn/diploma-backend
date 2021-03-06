package com.nulp.fetchproductdata.parser.allo.product;

import com.nulp.fetchproductdata.parser.allo.product.model.ProductData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AlloProductDataParser {

  public ProductData parseDataFromProductPage(String productPageUrl) {
    try {
      Document htmlDocument = Jsoup.connect(productPageUrl).get();

      return extractProductDataFromHtmlDocument(htmlDocument);
    } catch (IOException e) {
      log.warn("IO Error: " + e.getMessage());
    } catch (Exception e) {
      log.warn("Error while trying to scrape price for product: " + productPageUrl);
    }
    return null;
  }

  private ProductData extractProductDataFromHtmlDocument(Document document) {
    if (document.getElementsByClass("p-trade__price-box").size() == 0) {
      return null;
    }
    Element priceDataElement = document.getElementsByClass("p-trade__price-box").get(0);

    Element priceDetailsEl = priceDataElement.children().get(1);

    String oldPrice = "0.0", discount = "";
    Element currentPriceDiv;
    if (priceDetailsEl.children().size() == 1) {
      currentPriceDiv = priceDetailsEl.children().get(0);
    } else {
      Element oldPriceDiv = priceDetailsEl.children().get(0);
      oldPrice = oldPriceDiv.children().get(0).text().replaceAll(" ", "");
      oldPrice = oldPrice.replaceAll("[^0-9]", "");
      discount = oldPriceDiv.children().get(1).text();
      currentPriceDiv = priceDetailsEl.children().get(1);
    }
    String currentPrice = currentPriceDiv.children().get(1).attr("content");
    String currency = currentPriceDiv.children().get(2).attr("content");
    String availability = currentPriceDiv.children().get(3).attr("content");
    String priceValidUntil = currentPriceDiv.children().get(4).attr("content");

    String sku = document.getElementsByAttributeValue("itemprop", "sku").get(0).attr("content");
    String title = document.getElementsByAttributeValue("itemprop", "name").get(0).text();

    return new ProductData(
        sku,
        title,
        Double.parseDouble(oldPrice),
        discount,
        Double.parseDouble(currentPrice),
        currency,
        availability,
        priceValidUntil);
  }
}
