package com.nulp.fetchproductdata.parser.rozetka.price;

import com.google.gson.Gson;
import com.nulp.fetchproductdata.common.WebClient;
import com.nulp.fetchproductdata.parser.rozetka.price.model.Price;
import org.springframework.stereotype.Component;

@Component
public class RozetkaPriceParser {

  private final String apiLink = "https://common-api.rozetka.com.ua/v2/goods/get-price/?id=";

  public Price getPriceById(long productId) {
    String json = WebClient.getApiResponse(apiLink + productId);

    return translateJsonToPriceModel(json);
  }

  private Price translateJsonToPriceModel(String json) {
    return new Gson().fromJson(json, Price.class);
  }
}
