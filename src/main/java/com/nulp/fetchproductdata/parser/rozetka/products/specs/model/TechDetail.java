package com.nulp.fetchproductdata.parser.rozetka.products.specs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class TechDetail {

    private final int id;

    private final String type;

    private final String title;

    @SerializedName("category_id")
    private final int categoryId;

    @SerializedName("name")
    private final String codeName;

    private final List<DetailValue> values;
}
