package com.paulo.estudandoconfig.dto;

import java.math.BigDecimal;
import java.util.List;

import com.paulo.estudandoconfig.model.Role;

public class UserAccountDTO {

	private Long id;
	private String userName;
	private String password;
	private String name;
	private BigDecimal monthlyGoal;

	private List<String> rolesName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getRolesName() {
		return rolesName;
	}
	public void setRolesName(List<String> roles) {
		this.rolesName = roles;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public BigDecimal getMonthlyGoal() {
		return monthlyGoal;
	}
	public void setMonthlyGoal(BigDecimal monthlyGoal) {
		this.monthlyGoal = monthlyGoal;
	}
}
