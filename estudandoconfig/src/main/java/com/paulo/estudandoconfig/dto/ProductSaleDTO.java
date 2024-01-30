package com.paulo.estudandoconfig.dto;

public class ProductSaleDTO {

	private Long id;
	private Long productId;
	private String productName;
	private String name;
	private Integer quantity;

	public Long getId() {
		return id;
	}

	public ProductSaleDTO setId(Long id) {
		this.id = id;
		return this;

	}

	public Long getProductId() {
		return productId;
	}

	public ProductSaleDTO setProductId(Long productId) {
		this.productId = productId;
		return this;

	}

	public Integer getQuantity() {
		return quantity;
	}

	public ProductSaleDTO setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;

	}

	public String getProductName() {
		return productName;
	}

	public ProductSaleDTO setProductName(String productName) {
		this.productName = productName;
		return this;

	}

	public String getName() {
		return name;
	}

	public ProductSaleDTO setName(String name) {
		this.name = name;
		return this;
	}

}
