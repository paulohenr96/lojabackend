package com.paulo.estudandoconfig.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

	
	@NotBlank(message="{logindto.username.notBlank}")
	private String username;
	@NotBlank(message="{logindto.password.notBlank}")
	private String password;
	
	public String getUsername() {
		return username;
	}
	public LoginDTO setUsername(String username) {
		this.username = username;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public LoginDTO setPassword(String password) {
		this.password = password;
		return this;
	}
	
	
}
