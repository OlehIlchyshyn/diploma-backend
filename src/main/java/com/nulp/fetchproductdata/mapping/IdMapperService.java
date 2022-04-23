package com.nulp.fetchproductdata.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdMapperService {

  private final MapRepository mapRepository;

  //  private final Map<String, Integer> rozetkaProductIdByTitle = new HashMap<>();
  //  private final Map<String, Integer> rozetkaCategoryIdByTitle = new HashMap<>();
  //
  //  public Integer getRozetkaProductIdByTitle(String title) {
  //    return rozetkaProductIdByTitle.get(title);
  //  }
  //
  //  public void addRozetkaProductEntry(String title, Integer id) {
  //    rozetkaProductIdByTitle.put(title, id);
  //  }
  //
  //  public Integer getRozetkaCategoryIdByTitle(String title) {
  //    return rozetkaCategoryIdByTitle.get(title);
  //  }
  //
  //  public void addRozetkaCategoryEntry(String title, Integer id) {
  //    rozetkaCategoryIdByTitle.put(title, id);
  //  }

  public Integer getRozetkaProductIdByTitle(String title) {
    return mapRepository.findById(title).get().getValue();
  }

  public void addRozetkaProductEntry(String title, Integer id) {
    mapRepository.save(new MapEntry(title, id));
  }

  public Integer getRozetkaCategoryIdByTitle(String title) {
    return mapRepository.findById(title).get().getValue();
  }

  public void addRozetkaCategoryEntry(String title, Integer id) {
    mapRepository.save(new MapEntry(title, id));
  }
}
