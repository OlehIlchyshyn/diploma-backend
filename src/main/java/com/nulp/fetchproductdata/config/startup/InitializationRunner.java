package com.nulp.fetchproductdata.config.startup;

import com.nulp.fetchproductdata.config.Properties;
import com.nulp.fetchproductdata.service.initialization.InitSearchIndexesService;
import com.nulp.fetchproductdata.service.initialization.InitializationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitializationRunner implements ApplicationRunner {

  private final InitializationService initializationService;
  private final InitSearchIndexesService initSearchIndexesService;
  private final Properties properties;

  @Override
  public void run(ApplicationArguments args) {

    if (properties.isInitConversionRates()) {
      log.info("Started initializing conversion rates");
      initializationService.initConversionRates();
      log.info("Finished initializing conversion rates");
    }

    if (properties.isInitCategories()) {
      log.info("Started initializing categories");
      initializationService.initCategories();
      log.info("Finished initializing categories");
    }

    if (properties.isRebuildSearchIndexes()) {
      log.info("Started rebuilding search indexes");
      initSearchIndexesService.initProductIndex();
      log.info("Finished rebuilding search indexes");
    }
  }
}
