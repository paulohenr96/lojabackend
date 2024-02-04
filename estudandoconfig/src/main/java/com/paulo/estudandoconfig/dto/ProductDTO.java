package com.paulo.estudandoconfig.dto;

import java.math.BigDecimal;

public class ProductDTO {
	private Long id;
	private String name;
	private Integer quantity;
	private BigDecimal price;
	private String category;
	public String getName() {
		return name;
	}
	public ProductDTO setName(String name) {
		this.name = name;return this;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public ProductDTO setQuantity(Integer quantity) {
		this.quantity = quantity;return this;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public ProductDTO setPrice(BigDecimal price) {
		this.price = price;return this;
	}
	public Long getId() {
		return id;
	}
	public ProductDTO setId(Long id) {
		this.id = id;return this;
	}
	public String getCategory() {
		return category;
	}
	public ProductDTO setCategory(String category) {
		this.category = category;return this;
	}
	
	
}
