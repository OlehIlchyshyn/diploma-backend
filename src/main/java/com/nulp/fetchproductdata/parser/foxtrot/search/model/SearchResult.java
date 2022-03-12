package com.nulp.fetchproductdata.parser.foxtrot.search.model;

import lombok.Data;

@Data
public class SearchResult {

  private final boolean searchSuccessful;

  private String title;

  private String url;
}
