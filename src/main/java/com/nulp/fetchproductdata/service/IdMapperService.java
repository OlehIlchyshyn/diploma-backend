package com.nulp.fetchproductdata.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IdMapperService {

  // todo: store mapping in a databse
  private final Map<String, Integer> rozetkaProductIdByTitle = new HashMap<>();
  private final Map<String, Integer> rozetkaCategoryIdByTitle = new HashMap<>();

  public Integer getRozetkaProductIdByTitle(String title) {
    return rozetkaProductIdByTitle.get(title);
  }

  public void addRozetkaProductEntry(String title, Integer id) {
    rozetkaProductIdByTitle.put(title, id);
  }

  public Integer getRozetkaCategoryIdByTitle(String title) {
    return rozetkaCategoryIdByTitle.get(title);
  }

  public void addRozetkaCategoryEntry(String title, Integer id) {
    rozetkaCategoryIdByTitle.put(title, id);
  }
}
