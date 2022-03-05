package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.api.response.Catalog;
import com.nulp.fetchproductdata.parser.rozetka.categories.RozetkaCategoriesParser;
import com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrieveCategoriesService {

    private final RozetkaCategoriesParser categoriesParser;
    private final ModelMapper modelMapper;

    public Catalog getCatalog() {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(Category.class, com.nulp.fetchproductdata.api.response.Category.class)
                .addMappings(m -> m.map(Category::getChildren, com.nulp.fetchproductdata.api.response.Category::setSubCategories));

        List<Category> rootCategories = categoriesParser.fetchCategoriesFromApi();
        return new Catalog(rootCategories.stream()
                .map(category -> modelMapper.map(category, com.nulp.fetchproductdata.api.response.Category.class))
                .collect(Collectors.toList()));
    }
}
