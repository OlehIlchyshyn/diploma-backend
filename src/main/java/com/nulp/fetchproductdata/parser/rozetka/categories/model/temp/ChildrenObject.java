package com.nulp.fetchproductdata.parser.rozetka.categories.model.temp;

import lombok.Data;

import java.util.List;

@Data
public class ChildrenObject {
    private List<SubCategory> one;
    private List<SubCategory> two;
    private List<SubCategory> three;
}
