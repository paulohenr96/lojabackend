package com.paulo.estudandoconfig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.paulo.estudandoconfig.filter.FilterAuthentication;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable())
//cors configuration
				.addFilterAfter(new FilterAuthentication(), UsernamePasswordAuthenticationFilter.class)

// auth configuration	
//		.cors(c -> {
//			c.disable();
//			c.configurationSource(corsConfigurationSource());
//
//		})			
				
				
				.authorizeHttpRequests((authorize) -> authorize.requestMatchers("login").permitAll()
						.requestMatchers("users").hasAuthority("admin")
//						.requestMatchers("sales").authenticated()
//						.requestMatchers("products").authenticated()
						.anyRequest().authenticated())
					.cors(Customizer.withDefaults());
		return http.build();
	}

//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//	  CorsConfiguration configuration = new CorsConfiguration();
//	  configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//	  configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
//	  configuration.setAllowCredentials(true);
//	  configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type"));
//	  configuration.setExposedHeaders(Arrays.asList("X-Get-Header"));
//	  configuration.setMaxAge(3600L);
//	  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	  source.registerCorsConfiguration("/**", configuration);
//	  return source;
//	}

}
