package com.paulo.estudandoconfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paulo.estudandoconfig.model.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
