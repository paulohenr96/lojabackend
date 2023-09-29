package com.paulo.estudandoconfig.dto;

import java.math.BigDecimal;

public class RateDTO {
	
	private BigDecimal totalMonth;
	private BigDecimal averageMonths;
	private Integer numberOfSalesMonth;
	private Integer numberOfProducts;
	public BigDecimal getTotalMonth() {
		return totalMonth;
	}
	public void setTotalMonth(BigDecimal totalMonth) {
		this.totalMonth = totalMonth;
	}
	public BigDecimal getAverageMonths() {
		return averageMonths;
	}
	public void setAverageMonths(BigDecimal averageMonths) {
		this.averageMonths = averageMonths;
	}
	public Integer getNumberOfSalesMonth() {
		return numberOfSalesMonth;
	}
	public void setNumberOfSalesMonth(Integer numberOfSalesMonth) {
		this.numberOfSalesMonth = numberOfSalesMonth;
	}
	public Integer getNumberOfProducts() {
		return numberOfProducts;
	}
	public void setNumberOfProducts(Integer numberOfProducts) {
		this.numberOfProducts = numberOfProducts;
	}
	
}
