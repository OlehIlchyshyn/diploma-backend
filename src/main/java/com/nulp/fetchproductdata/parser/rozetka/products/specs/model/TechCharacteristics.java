package com.nulp.fetchproductdata.parser.rozetka.products.specs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class TechCharacteristics {

    @SerializedName("data")
    private final List<TechCharacteristicsGroup> techCharacteristicsGroupList;
}
