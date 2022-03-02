package com.nulp.fetchproductdata.parser.rozetka.categories;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category;
import com.nulp.fetchproductdata.parser.rozetka.categories.model.temp.RootCategory;
import com.nulp.fetchproductdata.parser.rozetka.categories.model.temp.SubCategory;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class RozetkaCategoriesParser {
    private final String categoriesApiUrl = "https://common-api.rozetka.com.ua/v2/fat-menu/full";
    private final Gson gson = new Gson();
    private final ModelMapper modelMapper = new ModelMapper();

    public List<Category> fetchCategoriesFromApi() {
        String json = getApiResponse();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class).getAsJsonObject("data");
        List<RootCategory> rootCategories = jsonObject
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .map(categoryObject -> gson.fromJson(categoryObject, RootCategory.class))
                .collect(Collectors.toList());

        List<Category> categories = cleanUpCategories(rootCategories);
        return categories;
    }

    private List<Category> cleanUpCategories(List<RootCategory> categories) {
        return categories.stream()
                .map(rootCategory -> {
                    Category category = modelMapper.map(rootCategory, Category.class);
                    if (rootCategory.getChildren() != null) {
                        category.setChildren(Stream.of(
                                        getChildrenCategories(rootCategory.getChildren().getOne()),
                                        getChildrenCategories(rootCategory.getChildren().getTwo()),
                                        getChildrenCategories(rootCategory.getChildren().getThree()))
                                .flatMap(List::stream)
                                .collect(Collectors.toList()));
                    }
                    return category;
                })
                .collect(Collectors.toList());
    }

    private List<Category> getChildrenCategories(List<SubCategory> list) {
        return list.stream()
                .map(subCategory -> {
                    Category category = modelMapper.map(subCategory, Category.class);
                    category.setChildren(subCategory.getChildren().stream()
                            .map(children -> modelMapper.map(children, Category.class))
                            .collect(Collectors.toList()));
                    return category;
                })
                .collect(Collectors.toList());
    }

    private String getApiResponse() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(categoriesApiUrl))
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
