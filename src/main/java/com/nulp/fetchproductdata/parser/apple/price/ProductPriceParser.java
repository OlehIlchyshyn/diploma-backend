package com.nulp.fetchproductdata.parser.apple.price;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
public class ProductPriceParser {

    private final String apiLink = "https://www.apple.com/us/shop/mcm/product-price?parts=";

    public double getPriceBySku(String sku) {
        return getPriceFromJson(getApiResponse(apiLink + sku));
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

    private String getApiResponse(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            log.error("Error while fetching data from product price API: ", e);
        } catch (InterruptedException e) {
            log.error("Thread was interrupted while fetching data.");
        }

        return response.body();
    }
}
