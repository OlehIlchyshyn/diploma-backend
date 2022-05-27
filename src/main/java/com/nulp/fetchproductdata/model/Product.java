package com.nulp.fetchproductdata.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Indexed
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @FullTextField private String fullName;

  @Column(columnDefinition = "text")
  private String description;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, Map<String, String>> techSpecs;

  private Double averagePrice;

  @OneToMany(cascade = CascadeType.PERSIST)
  private List<Price> priceList;

  private String imageUrl;

  @ManyToMany(mappedBy = "products")
  private Set<Category> categories;

  @PrePersist
  @PreUpdate
  private void prePersist() {
    this.averagePrice =
        priceList.stream().map(Price::getAmount).reduce(Double::sum).orElse(0.0) / priceList.size();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Product)) return false;
    Product product = (Product) o;
    return Objects.equals(id, product.id) && fullName.equals(product.fullName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fullName);
  }
}
