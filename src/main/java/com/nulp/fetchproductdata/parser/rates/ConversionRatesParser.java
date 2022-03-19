package com.nulp.fetchproductdata.parser.rates;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nulp.fetchproductdata.common.WebClient;
import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.parser.rates.model.ConversionRate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConversionRatesParser {

  private final String apiUrl = "https://free.currconv.com/api/v7/convert";
  private final String API_KEY = "31f58246253777e2741f";
  private final Gson gson = new Gson();

  public List<ConversionRate> getConversionRates() {
    List<ConversionRate> conversionRates = new LinkedList<>();
    for (var fromCurrency : Currency.values()) {
      for (var toCurrency :
          Arrays.stream(Currency.values())
              .filter(currency -> !currency.equals(fromCurrency))
              .collect(Collectors.toSet())) {
        String combination = fromCurrency.name() + "_" + toCurrency.name();
        String urlWithParams = getRequestUrl(combination);

        String json = WebClient.getApiResponse(urlWithParams);
        JsonObject conversionRatesObject = gson.fromJson(json, JsonObject.class);
        conversionRates.add(
            getConversionRate(fromCurrency, toCurrency, combination, conversionRatesObject));
      }
    }

    return conversionRates;
  }

  private String getRequestUrl(String firstCombination) {
    return UriComponentsBuilder.fromUriString(apiUrl)
        .queryParam("q", firstCombination)
        .queryParam("compact", "ultra")
        .queryParam("apiKey", API_KEY)
        .build()
        .toUriString();
  }

  private ConversionRate getConversionRate(
      Currency fromCurrency,
      Currency toCurrency,
      String secondCombination,
      JsonObject conversionRatesObject) {
    return ConversionRate.builder()
        .fromCurrency(fromCurrency)
        .toCurrency(toCurrency)
        .rate(conversionRatesObject.getAsJsonPrimitive(secondCombination).getAsDouble())
        .build();
  }
}
