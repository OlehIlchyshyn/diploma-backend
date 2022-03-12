package com.nulp.fetchproductdata.parser.rozetka.products.specs;

import com.google.gson.Gson;
import com.nulp.fetchproductdata.common.WebClient;
import com.nulp.fetchproductdata.parser.rozetka.products.specs.model.TechCharacteristics;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RozetkaTechCharacteristicsParser {

  private final String apiLink = "https://product-api.rozetka.com.ua/v4/goods/get-characteristic";
  private final Gson gson = new Gson();

  public TechCharacteristics getTechCharacteristicsByProductId(int productId) {
    String uriWithIdParam =
        UriComponentsBuilder.fromUriString(apiLink)
            .queryParam("goodsId", productId)
            .queryParam("lang", "ua")
            .build()
            .toUriString();

    String json = WebClient.getApiResponse(uriWithIdParam);

    return translateToModel(json);
  }

  private TechCharacteristics translateToModel(String json) {
    return gson.fromJson(json, TechCharacteristics.class);
  }
}
