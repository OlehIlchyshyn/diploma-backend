package com.nulp.fetchproductdata.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String fullName;

  @Column(columnDefinition = "text")
  private String description;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Map<String, String>> techSpecs;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Price> priceList;
}
