package com.nulp.fetchproductdata.parser.rozetka.categories;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nulp.fetchproductdata.common.WebClient;
import com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category;
import com.nulp.fetchproductdata.parser.rozetka.categories.model.temp.RootCategory;
import com.nulp.fetchproductdata.parser.rozetka.categories.model.temp.SubCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class RozetkaCategoriesParser {
  private final String categoriesApiUrl =
      "https://common-api.rozetka.com.ua/v2/fat-menu/full?lang=ua";
  private final Gson gson = new Gson();
  private final ModelMapper modelMapper;

  public List<Category> fetchCategoriesFromApi() {
    String json = WebClient.getApiResponse(categoriesApiUrl);
    JsonObject jsonObject = gson.fromJson(json, JsonObject.class).getAsJsonObject("data");
    List<RootCategory> rootCategories =
        jsonObject.entrySet().stream()
            .map(Map.Entry::getValue)
            .map(categoryObject -> gson.fromJson(categoryObject, RootCategory.class))
            .collect(Collectors.toList());

    return cleanUpCategories(rootCategories);
  }

  private List<Category> cleanUpCategories(List<RootCategory> categories) {
    return categories.stream()
        .map(
            rootCategory -> {
              Category category = modelMapper.map(rootCategory, Category.class);
              if (rootCategory.getChildren() != null) {
                category.setChildren(
                    Stream.of(
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
        .map(
            subCategory -> {
              Category category = modelMapper.map(subCategory, Category.class);
              category.setChildren(
                  subCategory.getChildren().stream()
                      .map(children -> modelMapper.map(children, Category.class))
                      .collect(Collectors.toList()));
              return category;
            })
        .collect(Collectors.toList());
  }
}
