package com.paulo.estudandoconfig.context;

import org.springframework.security.core.context.SecurityContextHolder;

public class ContextHolder {

	public static boolean isAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(roles -> roles.getAuthority().equals("admin"));
	}
	
	public static String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}
