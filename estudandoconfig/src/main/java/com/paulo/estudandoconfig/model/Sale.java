package com.paulo.estudandoconfig.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Sale implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2574840083903428392L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<ProductSale> products;
	private String buyer;
	private LocalDateTime date;
	private BigDecimal totalPrice;
	private String owner;
	
	public Long getId() {
		return id;
	}
	public Sale setId(Long id) {
		this.id = id;
		return this;
	}
	public List<ProductSale> getProducts() {
		return products;
	}
	public Sale setProducts(List<ProductSale> products) {
		this.products = products;
		return this;
	}
	public String getBuyer() {
		return buyer;
	}
	public Sale setBuyer(String buyer) {
		this.buyer = buyer;
		return this;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public Sale setDate(LocalDateTime date) {
		this.date = date;
		return this;
	}
	
	public Sale setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
		return this;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	
	
	public String getOwner() {
		return owner;
	}
	public Sale setOwner(String owner) {
		this.owner = owner;
		return this;
	}
	
}
