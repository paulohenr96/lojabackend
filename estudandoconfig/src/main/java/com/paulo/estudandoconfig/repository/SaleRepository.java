package com.paulo.estudandoconfig.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paulo.estudandoconfig.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {

	
	@Query(nativeQuery =true,value = "select sum(total_price) from sale where MONTH(date)=:month")
	BigDecimal totalIncome(Integer month);

}
