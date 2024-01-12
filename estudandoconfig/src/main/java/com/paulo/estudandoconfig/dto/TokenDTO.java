package com.paulo.estudandoconfig.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TokenDTO {

	private List<String> roles=new ArrayList<>();
	private Date issuedDate=Date.from(Instant.now());
	
	private String subject="";
	
	private String fullToken="";
	private BigDecimal goal;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFullToken() {
		return fullToken;
	}

	public void setFullToken(String fullToken) {
		this.fullToken = fullToken;
	}

	public BigDecimal getGoal() {
		return goal;
	}

	public void setGoal(BigDecimal goal) {
		this.goal = goal;
	}

	
	
	
	
}
