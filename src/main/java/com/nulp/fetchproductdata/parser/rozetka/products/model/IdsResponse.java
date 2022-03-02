package com.nulp.fetchproductdata.parser.rozetka.products.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class IdsResponse {

    private final List<Long> ids;

    @SerializedName("ids_count")
    private final int count;

    @SerializedName("total_pages")
    private final int pagesCount;
}
