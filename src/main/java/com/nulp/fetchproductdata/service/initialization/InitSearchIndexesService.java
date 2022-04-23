package com.nulp.fetchproductdata.service.initialization;

import com.nulp.fetchproductdata.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Slf4j
public class InitSearchIndexesService {

  @PersistenceContext private EntityManager entityManager;

  @Transactional
  public void initProductIndex() {
    SearchSession searchSession = Search.session(entityManager);
    MassIndexer indexer = searchSession.massIndexer(Product.class);
    try {
      indexer.startAndWait();
    } catch (InterruptedException e) {
      log.warn("Error while indexing Person class for full-text search");
    }
  }
}
