package com.nulp.fetchproductdata.mapping;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MapRepository extends JpaRepository<MapEntry, String> {}
