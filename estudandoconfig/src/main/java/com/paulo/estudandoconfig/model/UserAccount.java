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
	@GeneratedValue(strategy=GenerationType.AUTO)
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
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public BigDecimal getMonthlyGoal() {
		return monthlyGoal;
	}
	public void setMonthlyGoal(BigDecimal monthlyGoal) {
		this.monthlyGoal = monthlyGoal;
	}
	public UserAccountDTO toDTO() {
		UserAccountDTO dto = new ModelMapper().map(this, UserAccountDTO.class);
		dto.setRolesName(this.getRoles().stream().map(Role::getName).toList());

		return dto;
	}
}
