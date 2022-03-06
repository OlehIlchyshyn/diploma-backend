package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.api.response.Catalog;
import com.nulp.fetchproductdata.parser.rozetka.categories.RozetkaCategoriesParser;
import com.nulp.fetchproductdata.api.response.Category;
import com.nulp.fetchproductdata.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrieveCategoriesService {

    private final RozetkaCategoriesParser categoriesParser;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public Catalog getCatalog(boolean reindexCategories) {
        if (reindexCategories) {
            categoryRepository.deleteAll();
            List<com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category> rootCategories =
                    categoriesParser.fetchCategoriesFromApi();
            categoryRepository.saveAll(rootCategories.stream()
                    .map(category -> modelMapper.map(category, com.nulp.fetchproductdata.model.Category.class))
                    .collect(Collectors.toList()));
        }

        return new Catalog(mapToResponse(categoryRepository.findAll()));
    }

    private List<Category> mapToResponse(List<com.nulp.fetchproductdata.model.Category> categories) {
        return categories.stream()
                .map(category -> modelMapper.map(category, Category.class))
                .collect(Collectors.toList());
    }
}
