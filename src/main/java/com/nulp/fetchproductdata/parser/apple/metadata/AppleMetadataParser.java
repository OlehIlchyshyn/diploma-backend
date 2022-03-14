package com.nulp.fetchproductdata.parser.apple.metadata;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nulp.fetchproductdata.parser.apple.metadata.model.AppleProductMetadata;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppleMetadataParser {

  private final List<String> links =
      List.of(
          "https://www.apple.com/shop/buy-ipad/ipad-mini",
          "https://www.apple.com/shop/buy-ipad/ipad-air",
          "https://www.apple.com/shop/buy-ipad/ipad-pro",
          "https://www.apple.com/us-edu/shop/buy-mac/macbook-pro",
          "https://www.apple.com/shop/buy-mac/macbook-air",
          "https://www.apple.com/us-edu/shop/buy-iphone/iphone-13-pro",
          "https://www.apple.com/us-edu/shop/buy-iphone/iphone-13");

  public List<AppleProductMetadata> fetchProductMetadata() {
    return links.stream()
        .map(
            (linkUrl) -> {
              Document document = fetchDocument(linkUrl);
              return parseMetricsData(document);
            })
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private Document fetchDocument(String url) {
    Document document = null;
    try {
      document = Jsoup.connect(url).get();
    } catch (IOException e) {
      log.error("Error getting HTML data for url: " + url);
    }
    return document;
  }

  private List<AppleProductMetadata> parseMetricsData(Document document) {
    if (document == null) {
      return Collections.emptyList();
    }

    JsonObject jsonObject =
        new Gson()
            .fromJson(
                document.getElementById("metrics").childNodes().get(0).attr("#data"),
                JsonObject.class);
    JsonArray products = jsonObject.getAsJsonObject("data").getAsJsonArray("products");
    List<AppleProductMetadata> metadataList = new ArrayList<>(products.size());
    for (int i = 0; i < products.size(); ++i) {
      AppleProductMetadata product =
          new Gson().fromJson(products.get(i), AppleProductMetadata.class);
      if (product.getName() != null || product.getSku() != null) {
        metadataList.add(product);
      }
    }
    return metadataList;
  }
}
