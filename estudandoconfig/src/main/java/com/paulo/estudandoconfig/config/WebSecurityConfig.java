package com.paulo.estudandoconfig.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.paulo.estudandoconfig.filter.FilterAuthentication;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true,jsr250Enabled = true)
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf((csrf) -> csrf
						.disable())
				.cors().and()
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("login").permitAll()
						.requestMatchers("users").hasAuthority("admin")
						.anyRequest().authenticated()
				).addFilterAfter(new FilterAuthentication(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
//	 @Bean
//	 CorsConfigurationSource corsConfigurationSource() {
//	   CorsConfiguration configuration = new CorsConfiguration();
//	   configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//	   configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
//	   configuration.setAllowCredentials(true);
//	   configuration.setAllowedHeaders(Arrays.asList("authorization", "Requestor-Type"));
//	   configuration.setExposedHeaders(Arrays.asList("X-Get-Header"));
//	   configuration.setMaxAge(3600L);
//	   UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	   source.registerCorsConfiguration("/**", configuration);
//	   return source;
//	 }

}
