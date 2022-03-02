package com.nulp.fetchproductdata.common.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String model;

    private String fullName;

    private String description;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> techSpecs;

    @OneToMany
    private List<Price> priceList;
}
