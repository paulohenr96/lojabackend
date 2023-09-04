package com.paulo.estudandoconfig.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SaleDTO {
	
	
	private Long id;
	
	private LocalDateTime date;
	
	private String buyer;
	private List<ProductSaleDTO> products;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
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
	
}
