package com.nulp.fetchproductdata.repository;

import com.nulp.fetchproductdata.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

  PriceHistory getPriceHistoryByProductId(Long productId);
}
