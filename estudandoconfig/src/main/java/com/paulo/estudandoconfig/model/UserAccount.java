package com.paulo.estudandoconfig.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import org.modelmapper.ModelMapper;

import com.paulo.estudandoconfig.dto.UserAccountDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class UserAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3893489977033070350L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String userName;
	private String password;
	private BigDecimal monthlyGoal;

	@ManyToMany
	private Set<Role> roles;

	public Long getId() {
		return id;
	}

	public UserAccount setId(Long id) {
		this.id = id;
		return this;

	}

	public String getName() {
		return name;
	}

	public UserAccount setName(String name) {
		this.name = name;
		return this;

	}

	public String getUserName() {
		return userName;
	}

	public UserAccount setUserName(String userName) {
		this.userName = userName;
		return this;

	}

	public String getPassword() {
		return password;
	}

	public UserAccount setPassword(String password) {
		this.password = password;
		return this;

	}

	public Set<Role> getRoles() {
		return roles;
	}

	public UserAccount setRoles(Set<Role> roles) {
		this.roles = roles;
		return this;

	}

	public BigDecimal getMonthlyGoal() {
		return monthlyGoal;
	}

	public UserAccount setMonthlyGoal(BigDecimal monthlyGoal) {
		this.monthlyGoal = monthlyGoal;
		return this;

	}

}
