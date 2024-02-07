package com.paulo.estudandoconfig.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UserAccountDTO {

	private Long id;
	@NotBlank(message="{userdto.username.notBlank}")
	private String userName;
	@NotBlank(message="{userdto.password.notBlank}")
	private String password;
	@NotBlank(message="{userdto.name.notBlank}")
	private String name;
	@Min(value=0,message="{userdto.monthlyGoal.min}")
	private BigDecimal monthlyGoal;

	private List<String> rolesName;

	public String getUserName() {
		return userName;
	}

	public UserAccountDTO setUserName(String userName) {
		this.userName = userName;
		return this;

	}

	public String getPassword() {
		return password;
	}

	public UserAccountDTO setPassword(String password) {
		this.password = password;
		return this;

	}

	public String getName() {
		return name;
	}

	public UserAccountDTO setName(String name) {
		this.name = name;
		return this;

	}

	public List<String> getRolesName() {
		return rolesName;
	}

	public UserAccountDTO setRolesName(List<String> roles) {
		this.rolesName = roles;
		return this;

	}

	public UserAccountDTO setId(Long id) {
		this.id = id;
		return this;

	}

	public Long getId() {
		return id;
	}

	public BigDecimal getMonthlyGoal() {
		return monthlyGoal;
	}

	public UserAccountDTO setMonthlyGoal(BigDecimal monthlyGoal) {
		this.monthlyGoal = monthlyGoal;
		return this;
	}
}
