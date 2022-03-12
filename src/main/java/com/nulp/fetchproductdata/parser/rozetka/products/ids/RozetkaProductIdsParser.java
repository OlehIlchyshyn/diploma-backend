package com.nulp.fetchproductdata.parser.rozetka.products.ids;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nulp.fetchproductdata.common.WebClient;
import com.nulp.fetchproductdata.parser.rozetka.products.ids.model.IdsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class RozetkaProductIdsParser {

    private final String apiUrl = "https://xl-catalog-api.rozetka.com.ua/v4/goods/get";
    private final Gson gson = new Gson();

    public List<Integer> getProductIdsByCategory(long categoryId) {
        String urlByCategory = UriComponentsBuilder
                .fromUriString(apiUrl)
                .queryParam("category_id", categoryId)
                .build()
                .toUriString();

        String jsonResponse = WebClient.getApiResponse(urlByCategory);
        IdsResponse firstPage = getProductDataFromJson(jsonResponse);
        List<Integer> allIds = new ArrayList<>(firstPage.getCount());
        allIds.addAll(firstPage.getIds());

        for (int pageNumber = 2; pageNumber <= firstPage.getPagesCount(); ++pageNumber) {
            String urlWithPaging = UriComponentsBuilder
                    .fromUriString(apiUrl)
                    .queryParam("page", pageNumber)
                    .queryParam("category_id", categoryId)
                    .build()
                    .toUriString();
            IdsResponse productData = getProductDataFromJson(WebClient.getApiResponse(urlWithPaging));
            allIds.addAll(productData.getIds());
        }

        return allIds;
    }

    public List<Integer> getProductIdsByCategoryAndBrand(long categoryId, String producer) {
        String urlByCategoryAndProducer = UriComponentsBuilder
                .fromUriString(apiUrl)
                .queryParam("category_id", categoryId)
                .queryParam("producer", producer)
                .build()
                .toUriString();

        String jsonResponse = WebClient.getApiResponse(urlByCategoryAndProducer);
        IdsResponse firstPage = getProductDataFromJson(jsonResponse);
        List<Integer> allIds = new ArrayList<>(firstPage.getCount());
        allIds.addAll(firstPage.getIds());

        for (int pageNumber = 2; pageNumber <= firstPage.getPagesCount(); ++pageNumber) {
            String urlWithPaging = UriComponentsBuilder
                    .fromUriString(apiUrl)
                    .queryParam("page", pageNumber)
                    .queryParam("category_id", categoryId)
                    .queryParam("producer", producer)
                    .build()
                    .toUriString();
            IdsResponse productData = getProductDataFromJson(WebClient.getApiResponse(urlWithPaging));
            allIds.addAll(productData.getIds());
        }

        return allIds;
    }

    private IdsResponse getProductDataFromJson(String json) {
        JsonObject dataObject = gson.fromJson(json, JsonObject.class).getAsJsonObject("data");
        return gson.fromJson(dataObject, IdsResponse.class);
    }
}
