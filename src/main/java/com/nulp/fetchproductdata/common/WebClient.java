package com.nulp.fetchproductdata.common;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class WebClient {

  public static String getApiResponse(String url) {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder().version(HttpClient.Version.HTTP_2).uri(URI.create(url)).build();
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return response.body();
    } catch (ConnectException e) {
      log.error("Error while connecting to the API: " + e.getMessage());
    } catch (IOException e) {
      log.error("Error while fetching data from API: " + e.getMessage());
    } catch (InterruptedException e) {
      log.error("Thread was interrupted while fetching data.");
    }
    return "";
  }

  public static Document getDocument(String url) {
    try {
      return Jsoup.connect(url).get();
    } catch (MalformedURLException e) {
      log.warn(e.getMessage());
    } catch (IOException e) {
      log.error("Error while trying to scrap document from url: " + url);
    }
    return null;
  }
}
