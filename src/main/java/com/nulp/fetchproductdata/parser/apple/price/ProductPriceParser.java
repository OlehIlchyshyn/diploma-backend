package com.nulp.fetchproductdata.parser.apple.price;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nulp.fetchproductdata.common.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductPriceParser {

    private final String apiLink = "https://www.apple.com/us/shop/mcm/product-price?parts=";

    public double getPriceBySku(String sku) {
        return getPriceFromJson(WebClient.getApiResponse(apiLink + sku));
    }

    private double getPriceFromJson(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        return jsonObject
                .getAsJsonObject("items")
                .entrySet().stream().findFirst().get().getValue()
                .getAsJsonObject()
                .getAsJsonObject("price")
                .getAsJsonPrimitive("value")
                .getAsDouble();
    }
}
