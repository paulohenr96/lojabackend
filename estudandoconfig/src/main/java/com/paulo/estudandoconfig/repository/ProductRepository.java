package com.paulo.estudandoconfig.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paulo.estudandoconfig.model.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	
	@Query(nativeQuery = true,value = "SELECT SUM(quantity) from product")
	public Integer sumQuantity();
	
	Page<Product> findAllByCategory(PageRequest p,String category);
}
