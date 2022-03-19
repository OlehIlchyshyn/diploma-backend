package com.nulp.fetchproductdata.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/configuration.properties")
@Getter
public class Properties {

  @Value("${limits.categories:#{null}}")
  private Integer categoriesLimit;

  @Value("${limits.productPerSubCategory:#{null}}")
  private Integer productPerSubCategoryLimit;
}