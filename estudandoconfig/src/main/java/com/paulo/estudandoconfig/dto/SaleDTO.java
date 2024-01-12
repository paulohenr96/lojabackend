package com.paulo.estudandoconfig.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SaleDTO {
	
	
	private Long id;
	
	private LocalDateTime date;
	private BigDecimal totalPrice;

	private String buyer;
	private String owner;

	private List<ProductSaleDTO> products;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public List<ProductSaleDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductSaleDTO> products) {
		this.products = products;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
