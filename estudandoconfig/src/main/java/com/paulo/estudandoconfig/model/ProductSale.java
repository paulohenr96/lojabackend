package com.paulo.estudandoconfig.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ProductSale implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3873751450220021603L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	private Long productId;
	private Integer quantity;
	private String name;
	private BigDecimal unitPrice;

	public Long getId() {
		return id;
	}

	public ProductSale setId(Long id) {
		this.id = id;
		return this;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public ProductSale setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	public String getName() {
		return name;
	}

	public ProductSale setName(String name) {
		this.name = name;
		return this;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public ProductSale setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
		return this;
	}

	public Long getProductId() {
		return productId;
	}

	public ProductSale setProductId(Long productId) {
		this.productId = productId;
	return this;}
	
	
	
}
