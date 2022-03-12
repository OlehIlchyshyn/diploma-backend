package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.model.Price;
import com.nulp.fetchproductdata.service.priceprovider.PriceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final List<PriceProvider> priceProviders;

    public List<Price> getPriceByProductTitle(String title) {

        String polishedTitle;

        Pattern bracketsPatter = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = bracketsPatter.matcher(title);
        if (matcher.find()) {
            polishedTitle = matcher.group(1);
        } else {
            polishedTitle = title
                    .replaceAll("[а-яА-Я]", "")
                    .replaceAll("\"", "")
                    .replaceAll("'", "")
                    .replaceAll("\u200E", "")
                    .replaceAll(" ", "+")
                    .replaceAll("ㅤ", "+")
                    .replaceAll(" ", "+");
        }

        return priceProviders.stream()
                .map(priceProvider -> priceProvider.getPriceByProductTitle(polishedTitle))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
