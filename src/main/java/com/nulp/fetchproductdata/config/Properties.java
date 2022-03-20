package com.nulp.fetchproductdata.config;

import com.nulp.fetchproductdata.common.enumeration.Currency;
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

  @Value("${preferredCurrency:UAH}")
  private Currency preferredCurrency;

  @Value("${limits.outliersPercentage:50}")
  private double outliersPercentage;

  @Value("${startup.initCategories: false}")
  private boolean initCategories;

  @Value("${startup.initRates: false}")
  private boolean initConversionRates;
}
