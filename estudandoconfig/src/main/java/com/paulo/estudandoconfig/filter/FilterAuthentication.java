package com.paulo.estudandoconfig.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.paulo.estudandoconfig.JWTCreator;
import com.paulo.estudandoconfig.constant.Constants;
import com.paulo.estudandoconfig.dto.TokenDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FilterAuthentication extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
//		configureCors(response);
//		showHeadersAndValues(request);

//		String valueToken = request.getHeader("authorization");

		
		SecurityContextHolder.clearContext();
		
		
		Optional.ofNullable(request.getHeader("authorization"))
				.map(valueToken -> valueToken.replace(Constants.prefix, ""))
				.map(JWTCreator::verifyToken)
				.ifPresent(dto -> 
						SecurityContextHolder.getContext()
							.setAuthentication(new UsernamePasswordAuthenticationToken(dto.getSubject(), "",
								dto.getRoles().stream().map(SimpleGrantedAuthority::new).toList())));
		
		
		filterChain.doFilter(request, response);

	}

	private void showHeadersAndValues(HttpServletRequest request) {
		Enumeration<String> headerNames = request.getHeaderNames();
		System.out.println("-=-=-=-=-=-=-Headers => value -=-=-=-=-=-");
		while (headerNames.hasMoreElements()) {
			String nextElement = headerNames.nextElement();
			String value = request.getHeader(nextElement);
			System.out.println(nextElement + " => " + value);
		}
		System.out.println("-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-");
	}

	private void configureCors(HttpServletResponse response) {

		response.addHeader("Access-Control-Expose-Headers", "Authorization");
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");

		}
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");

		}
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");

		}

	}

}
