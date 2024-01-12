package com.paulo.estudandoconfig.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Metrics {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	private BigDecimal monthlyGoal;

	public BigDecimal getMonthlyGoal() {
		return monthlyGoal;
	}

	public void setMonthlyGoal(BigDecimal monthlyGoal) {
		this.monthlyGoal = monthlyGoal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
