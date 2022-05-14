package com.nulp.fetchproductdata.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;
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
  @ToString.Exclude
  private Set<Category> subCategories;

  @ManyToMany(cascade = CascadeType.PERSIST)
  @ToString.Exclude
  private Set<Product> products;

  @ManyToOne private Category parent;

  public void setSubCategories(Set<Category> subCategories) {
    if (subCategories != null) {
      subCategories.forEach(category -> category.setParent(this));
    }
    this.subCategories = subCategories;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Category)) return false;
    Category category = (Category) o;
    return Objects.equals(id, category.id) && Objects.equals(title, category.getTitle());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title);
  }
}
