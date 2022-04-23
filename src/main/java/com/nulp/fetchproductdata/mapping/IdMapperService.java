package com.nulp.fetchproductdata.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IdMapperService {

  private final MapRepository mapRepository;

  public Integer getRozetkaProductIdByTitle(String title) throws NoSuchElementException {
    return mapRepository.findById(title).orElseThrow().getValue();
  }

  public void addRozetkaProductEntry(String title, Integer id) {
    mapRepository.save(new MapEntry(title, id));
  }

  public Integer getRozetkaCategoryIdByTitle(String title) throws NoSuchElementException {
    return mapRepository.findById(title).orElseThrow().getValue();
  }

  public void addRozetkaCategoryEntry(String title, Integer id) {
    mapRepository.save(new MapEntry(title, id));
  }
}
