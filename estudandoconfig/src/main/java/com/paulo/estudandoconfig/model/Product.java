package com.paulo.estudandoconfig.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Product implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7689139971358663099L;
	@Id
	@SequenceGenerator(name="SEQ_GEN",sequenceName="SEQ_PRODUCT",allocationSize=1,initialValue = 300)
	@GeneratedValue(generator="SEQ_GEN")
	private Long id;
	private String name;
	private Integer quantity;
	private BigDecimal price;
	private String category;
	
	
	
	
	public Product() {
	}
	public Product(String name, Integer quantity, BigDecimal price) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}
	public Long getId() {
		return id;
	}
	public Product setId(Long id) {
		this.id = id;
		return this;

	}
	public String getName() {
		return name;
	}
	public Product setName(String name) {
		this.name = name;
		return this;

	}
	public Integer getQuantity() {
		return quantity;
	}
	public Product setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;

	}
	public BigDecimal getPrice() {
		return price;
	}
	public Product setPrice(BigDecimal price) {
		this.price = price;
		return this;

	}
	public String getCategory() {
		return category;
	}
	public Product setCategory(String category) {
		this.category = category;
		
		return this;
		
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
		Product other = (Product) obj;
		return Objects.equals(id, other.id);
	}
	
}
