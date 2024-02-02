package com.paulo.estudandoconfig.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
	public SaleDTO setId(Long id) {
		this.id = id;return this;
	}
	
	public String getBuyer() {
		return buyer;
	}
	public SaleDTO setBuyer(String buyer) {
		this.buyer = buyer;return this;
	}
	public List<ProductSaleDTO> getProducts() {
		return products;
	}
	public SaleDTO setProducts(List<ProductSaleDTO> products) {
		this.products = products;return this;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public SaleDTO setDate(LocalDateTime date) {
		this.date = date;return this;
	}
	
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public SaleDTO setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;return this;
	}
	public String getOwner() {
		return owner;
	}

	public SaleDTO setOwner(String owner) {
		this.owner = owner;return this;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaleDTO other = (SaleDTO) obj;
		return Objects.equals(id, other.id);
	}
	
}
