package com.nulp.fetchproductdata.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Category> subCategories = new HashSet<>();

    //todo revisit need of cascading here
    @ManyToMany(cascade=CascadeType.PERSIST)
    private List<Product> products;
}
