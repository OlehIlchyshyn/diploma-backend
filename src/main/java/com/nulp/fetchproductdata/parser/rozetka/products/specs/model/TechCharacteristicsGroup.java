package com.nulp.fetchproductdata.parser.rozetka.products.specs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class TechCharacteristicsGroup {

    @SerializedName("group_id")
    private final int groupId;

    @SerializedName("groupTitle")
    private final String groupTitle;

    @SerializedName("options")
    private final List<TechDetail> specs;
}
