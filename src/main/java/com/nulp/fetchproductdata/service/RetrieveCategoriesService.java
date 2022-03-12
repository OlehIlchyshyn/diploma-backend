package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.api.response.Catalog;
import com.nulp.fetchproductdata.model.Product;
import com.nulp.fetchproductdata.parser.rozetka.categories.RozetkaCategoriesParser;
import com.nulp.fetchproductdata.api.response.Category;
import com.nulp.fetchproductdata.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrieveCategoriesService {

    private final RozetkaCategoriesParser categoriesParser;
    private final CategoryRepository categoryRepository;
    private final IdMapperService idMapperService;
    private final ProductListService productListService;
    private final ModelMapper modelMapper;

    @Transactional
    public Catalog getCatalog(boolean reindexCategories, boolean includeSubcategories, boolean includeProductList) {
        if (reindexCategories) {
            List<com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category> rootCategories =
                    categoriesParser.fetchCategoriesFromApi();

            //todo remove temporary trim of the categories
            rootCategories = rootCategories.subList(0, 1);

            for (var rootCategory: rootCategories) {
                idMapperService.addRozetkaCategoryEntry(rootCategory.getTitle(), rootCategory.getCategoryId());
                for (var category: rootCategory.getChildren()) {
                    idMapperService.addRozetkaCategoryEntry(category.getTitle(), category.getCategoryId());
                }
            }

            List<com.nulp.fetchproductdata.model.Category> categories = mapToModel(rootCategories);

            for (var rootCategory: categories) {
                List<Product> rootCategoryProducts = new LinkedList<>();
                for (var category: rootCategory.getSubCategories()) {
                    int subCategoryId = idMapperService.getRozetkaCategoryIdByTitle(category.getTitle());
                    var subCategoryProducts = productListService.getProductsByCategoryId(subCategoryId);
                    rootCategoryProducts.addAll(subCategoryProducts);
                    category.setProducts(subCategoryProducts);
                }
                rootCategory.setProducts(rootCategoryProducts);
            }

            categoryRepository.deleteAll();
            categoryRepository.saveAll(categories);
        }

        return new Catalog(mapToResponse(categoryRepository.findAllParentCategories()));
    }

    private List<com.nulp.fetchproductdata.model.Category> mapToModel(
            List<com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category> rootCategories) {
        return rootCategories.stream()
                .map(category -> modelMapper.map(category, com.nulp.fetchproductdata.model.Category.class))
                .collect(Collectors.toList());
    }

    private List<Category> mapToResponse(List<com.nulp.fetchproductdata.model.Category> categories) {
        return categories.stream()
                .map(category -> modelMapper.map(category, Category.class))
                .collect(Collectors.toList());
    }
}
