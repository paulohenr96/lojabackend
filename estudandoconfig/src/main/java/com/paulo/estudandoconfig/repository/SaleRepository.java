package com.paulo.estudandoconfig.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paulo.estudandoconfig.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query(nativeQuery = true, value = "select sum(total_price) from sale where owner=:username AND  EXTRACT(MONTH from date)=:month")
	BigDecimal totalIncome(Integer month,String username);
	
	@Query(nativeQuery = true, value = "select sum(total_price) from sale where  EXTRACT(MONTH from date)=:month")
	BigDecimal totalIncome(Integer month);
	

	@Query(nativeQuery = true, value = "SELECT  EXTRACT(MONTH FROM date)as month , SUM(total_price) AS quantity from sale where owner=:username AND EXTRACT(YEAR FROM date)= :year GROUP by month")
	List<int[]> chartSaleMonth(Integer year,String username);

	
	@Query(nativeQuery = true, value = "SELECT  EXTRACT(MONTH FROM date)as month , SUM(total_price) AS quantity from sale where  EXTRACT(YEAR FROM date)= :year GROUP by month")
	List<int[]> chartSaleMonth(Integer year);
	
	Page<Sale>findAllByOwner(PageRequest pr,String username);
}
