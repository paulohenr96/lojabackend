package com.paulo.estudandoconfig.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TokenDTO {

	private List<String> roles = new ArrayList<>();
	private Date issuedDate = Date.from(Instant.now());

	private String subject = "";

	private String fullToken = "";
	private BigDecimal goal;

	public List<String> getRoles() {
		return roles;
	}

	public TokenDTO setRoles(List<String> roles) {
		this.roles = roles;
		return this;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public TokenDTO setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
		return this;

	}

	public String getSubject() {
		return subject;
	}

	public TokenDTO setSubject(String subject) {
		this.subject = subject;
		return this;

	}

	public String getFullToken() {
		return fullToken;
	}

	public TokenDTO setFullToken(String fullToken) {
		this.fullToken = fullToken;
		return this;

	}

	public BigDecimal getGoal() {
		return goal;
	}

	public TokenDTO setGoal(BigDecimal goal) {
		this.goal = goal;
		return this;

	}

}
