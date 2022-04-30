package com.nulp.fetchproductdata.parser.foxtrot.search;

import com.nulp.fetchproductdata.common.WebClient;
import com.nulp.fetchproductdata.parser.foxtrot.search.model.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class FoxtrotSearchResultsParser {

  private final String searchPageUrl = "https://www.foxtrot.com.ua/uk/search";
  private final String urlPrefix = "https://www.foxtrot.com.ua";

  public SearchResult searchByProductTitle(String title) {

    String searchQueryUrl =
        UriComponentsBuilder.fromUriString(searchPageUrl)
            .queryParam("query", title)
            .build()
            .toUriString();

    Document document = WebClient.getDocument(searchQueryUrl);
    if (document == null) {
      return new SearchResult(false);
    }

    if (isNoMatchFound(document)) {
      return new SearchResult(false);
    }

    if (document.location().contains("search")) {
      return parseSearchResultFromHtml(document);
    } else {
      return parseReturnedPageAsSearchResult(document);
    }
  }

  private boolean isNoMatchFound(Document document) {
    return !document.getElementsByClass("search-page__box-title").isEmpty()
        && document.getElementsByClass("search-page__box-title").text().contains("не знайдено");
  }

  private SearchResult parseSearchResultFromHtml(Document htmlDocument) {
    Elements cards = htmlDocument.getElementsByClass("card__title");
    if (cards.isEmpty()) {
      return new SearchResult(false);
    } else {
      Element match = cards.get(0);
      SearchResult searchResult = new SearchResult(true);
      searchResult.setTitle(match.attr("title"));
      searchResult.setUrl(urlPrefix + match.attr("href"));
      return searchResult;
    }
  }

  private SearchResult parseReturnedPageAsSearchResult(Document htmlDocument) {
    Element titleEl = htmlDocument.getElementById("product-page-title");
    if (titleEl == null) {
      return new SearchResult(false);
    } else {
      SearchResult searchResult = new SearchResult(true);
      searchResult.setTitle(titleEl.text());
      searchResult.setUrl(htmlDocument.location());
      return searchResult;
    }
  }
}
