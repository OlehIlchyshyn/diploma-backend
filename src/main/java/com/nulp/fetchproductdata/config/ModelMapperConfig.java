package com.nulp.fetchproductdata.config;

import com.nulp.fetchproductdata.parser.rozetka.categories.model.response.Category;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(Category.class, com.nulp.fetchproductdata.model.Category.class)
                .addMappings(m -> m.map(Category::getChildren,
                        com.nulp.fetchproductdata.model.Category::setSubCategories));
        modelMapper.typeMap(com.nulp.fetchproductdata.model.Category.class, com.nulp.fetchproductdata.api.response.Category.class)
                .addMappings(m -> m.map(com.nulp.fetchproductdata.model.Category::getSubCategories,
                        com.nulp.fetchproductdata.api.response.Category::setSubCategories))
                .addMappings(m -> m.map(com.nulp.fetchproductdata.model.Category::getProducts,
                        com.nulp.fetchproductdata.api.response.Category::setProductList));
        modelMapper.typeMap(com.nulp.fetchproductdata.model.Price.class, com.nulp.fetchproductdata.api.response.Price.class)
                .addMappings(m -> m.map(com.nulp.fetchproductdata.model.Price::getPriceProvider,
                        com.nulp.fetchproductdata.api.response.Price::setPriceProvider));
        modelMapper.typeMap(com.nulp.fetchproductdata.model.Product.class, com.nulp.fetchproductdata.api.response.Product.class)
                .addMappings(m -> m.map(com.nulp.fetchproductdata.model.Product::getPriceList,
                        com.nulp.fetchproductdata.api.response.Product::setPriceList));
        return modelMapper;
    }
}
