package com.nulp.fetchproductdata.parser.allo.search;

import com.nulp.fetchproductdata.common.WebClient;
import com.nulp.fetchproductdata.parser.foxtrot.search.model.SearchResult;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AlloSearchResultParser {

  private final String searchPageUrl = "https://allo.ua/ua/catalogsearch/result/";

  public SearchResult searchByProductTitle(String title) {
    String searchQueryUrl =
        UriComponentsBuilder.fromUriString(searchPageUrl)
            .queryParam("q", title)
            .build()
            .toUriString();

    Document document = WebClient.getDocument(searchQueryUrl);
    if (document == null) {
      return new SearchResult(false);
    }

    if (document.location().contains("products")) {
      return getSearchResultAsProductPage(document);
    }
    return parseSearchResultFromHtml(document);
  }

  private SearchResult getSearchResultAsProductPage(Document document) {
    SearchResult searchResult = new SearchResult(true);
    searchResult.setUrl(document.location());
    searchResult.setTitle(document.getElementsByClass("p-view__header-title").get(0).text());
    return searchResult;
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
