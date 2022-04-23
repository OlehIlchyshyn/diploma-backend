package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.model.Product;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class ProductSearchService {

  @PersistenceContext private EntityManager entityManager;

  @Transactional
  public List<Product> findProductsByTitle(String title) {
    SearchSession searchSession = Search.session(entityManager);

    SearchResult<Product> result =
        searchSession
            .search(Product.class)
            .where(f -> f.match().field("fullName").matching(title))
            .fetchAll();

    return result.hits();
  }
}
