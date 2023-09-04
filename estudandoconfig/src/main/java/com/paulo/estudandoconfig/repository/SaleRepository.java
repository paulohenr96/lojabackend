package com.paulo.estudandoconfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paulo.estudandoconfig.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {

}
