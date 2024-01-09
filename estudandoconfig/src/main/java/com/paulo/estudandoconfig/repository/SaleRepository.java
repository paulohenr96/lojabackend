package com.paulo.estudandoconfig.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paulo.estudandoconfig.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query(nativeQuery = true, value = "select sum(total_price) from sale where  EXTRACT(MONTH from date)=:month")
	BigDecimal totalIncome(Integer month);

	@Query(nativeQuery = true, value = "SELECT  EXTRACT(MONTH FROM date)as month , COUNT(*) AS quantity from sale where EXTRACT(YEAR FROM date)= :year GROUP by month")
	List<int[]> chartSaleMonth(Integer year);

}
