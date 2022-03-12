package com.nulp.fetchproductdata.parser.allo.product;

import com.nulp.fetchproductdata.parser.allo.product.model.ProductData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AlloProductDataParser {

  public ProductData parseDataFromProductPage(String productPageUrl) {
    try {
      Document htmlDocument = Jsoup.connect(productPageUrl).get();

      return extractProductDataFromHtmlDocument(htmlDocument);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private ProductData extractProductDataFromHtmlDocument(Document document) {

    Element priceDataElement = document.getElementsByClass("p-trade__price-box").get(0);
    Element itemAvailabilityEl = priceDataElement.children().get(0);
    String itemAvailability = itemAvailabilityEl.text();

    Element priceDetailsEl = priceDataElement.children().get(1);
    Element oldPriceDiv = priceDetailsEl.children().get(0);
    String oldPrice = oldPriceDiv.children().get(0).text().replaceAll(" ", "");
    oldPrice = oldPrice.substring(0, oldPrice.length() - 1);

    String discount = oldPriceDiv.children().get(1).text();
    Element currentPriceDiv = priceDetailsEl.children().get(1);
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
