package com.nulp.fetchproductdata.parser.allo.search;

import com.nulp.fetchproductdata.parser.foxtrot.search.model.SearchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class AlloSearchResultParser {

  private final String searchPageUrl = "https://allo.ua/ua/catalogsearch/result/";

  public SearchResult searchByProductTitle(String title) {
    String searchQueryUrl =
        UriComponentsBuilder.fromUriString(searchPageUrl)
            .queryParam("q", title)
            .build()
            .toUriString();

    try {
      Document document = Jsoup.connect(searchQueryUrl).get();
      return parseSearchResultFromHtml(document);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new SearchResult(false);
  }

  private SearchResult parseSearchResultFromHtml(Document htmlDocument) {
    Elements cards = htmlDocument.getElementsByClass("product-card__title");
    if (cards.isEmpty()) {
      return new SearchResult(false);
    } else {
      Element match = cards.get(0);
      SearchResult searchResult = new SearchResult(true);
      searchResult.setTitle(match.attr("title"));
      searchResult.setUrl(match.attr("href"));
      return searchResult;
    }
  }
}
