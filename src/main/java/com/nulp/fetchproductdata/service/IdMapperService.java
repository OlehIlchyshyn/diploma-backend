package com.nulp.fetchproductdata.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IdMapperService {

  // todo: store mapping in a databse
  private final Map<String, Integer> productTitle = new HashMap<>();
  private final Map<String, Integer> categoriesTitle = new HashMap<>();

  public Integer getRozetkaProductIdByTitle(String title) {
    return productTitle.get(title);
  }

  public void addRozetkaProductEntry(String title, Integer id) {
    productTitle.put(title, id);
  }

  public Integer getRozetkaCategoryIdByTitle(String title) {
    return categoriesTitle.get(title);
  }

  public void addRozetkaCategoryEntry(String title, Integer id) {
    categoriesTitle.put(title, id);
  }
}
