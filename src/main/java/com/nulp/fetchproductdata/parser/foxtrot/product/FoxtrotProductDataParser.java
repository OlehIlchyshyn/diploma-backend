package com.nulp.fetchproductdata.parser.foxtrot.product;

import com.google.gson.Gson;
import com.nulp.fetchproductdata.parser.foxtrot.product.model.ProductData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FoxtrotProductDataParser {

    public ProductData parseDataFromProductPage(String productPageUrl) {
        try {
            Document htmlDocument = Jsoup.connect(productPageUrl).get();
            String productDataJson = htmlDocument.getElementsByAttributeValue("name", "product_structured_data")
                    .get(0).data();

            return translateJsonToProductData(productDataJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ProductData translateJsonToProductData(String json){
        return new Gson().fromJson(json, ProductData.class);
    }
}
