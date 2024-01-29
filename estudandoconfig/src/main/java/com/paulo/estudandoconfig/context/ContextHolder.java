package com.paulo.estudandoconfig.context;

import org.springframework.security.core.context.SecurityContextHolder;


public class ContextHolder {

	public  boolean isAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(roles -> roles.getAuthority().equals("admin"));
	}
	
	public  String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public String responseJson(String s) {
	    return String.format("{\"response\":\"%s\"}", s);
	}


}
