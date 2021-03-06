package com.nulp.fetchproductdata.parser.rozetka.products.details;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nulp.fetchproductdata.common.WebClient;
import com.nulp.fetchproductdata.parser.rozetka.products.details.model.ProductDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RozetkaProductDetailsParser {

  private final String detailsApiUrl = "https://xl-catalog-api.rozetka.com.ua/v4/goods/getDetails";
  private final Gson gson = new Gson();

  public List<ProductDetails> getProductDetailsByProductId(List<Integer> productIds) {
    String uriWithIdsParam =
        UriComponentsBuilder.fromUriString(detailsApiUrl)
            .queryParam(
                "product_ids",
                productIds.stream().map(String::valueOf).collect(Collectors.joining(",")))
            .build()
            .toUriString();

    String json = WebClient.getApiResponse(uriWithIdsParam);
    if (json.equals("")) return Collections.emptyList();
    JsonArray dataArray = gson.fromJson(json, JsonObject.class).getAsJsonArray("data");
    Type productListType = new TypeToken<List<ProductDetails>>() {}.getType();
    return gson.fromJson(dataArray, productListType);
  }
}
