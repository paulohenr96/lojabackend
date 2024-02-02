package com.paulo.estudandoconfig.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Metrics implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1323056663405646364L;


	@Id
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	private BigDecimal monthlyGoal=BigDecimal.valueOf(0);

	public BigDecimal getMonthlyGoal() {
		return monthlyGoal;
	}

	public Metrics setMonthlyGoal(BigDecimal monthlyGoal) {
		this.monthlyGoal = monthlyGoal;
		return this;
	}

	public Long getId() {
		return id;
	}

	public Metrics setId(Long id) {
		this.id = id;		return this;

	}
	
}
