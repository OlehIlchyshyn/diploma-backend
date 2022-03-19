package com.nulp.fetchproductdata.config.startup;

import com.nulp.fetchproductdata.service.initialization.InitializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitializationRunner implements ApplicationRunner {

  private final InitializationService initializationService;

  @Override
  public void run(ApplicationArguments args) {
    initializationService.initCategories();
  }
}
