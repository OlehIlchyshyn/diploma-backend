package com.nulp.fetchproductdata.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String title;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Category> subCategories;

  @ManyToMany(cascade = CascadeType.PERSIST)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Product> products;

  @ManyToOne private Category parent;

  public void setSubCategories(Set<Category> subCategories) {
    if (subCategories != null) {
      subCategories.forEach(category -> category.setParent(this));
    }
    this.subCategories = subCategories;
  }
}
