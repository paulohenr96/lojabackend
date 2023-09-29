package com.paulo.estudandoconfig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.paulo.estudandoconfig.filter.FilterAuthentication;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	
	
	 
	 @Bean
		public SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception {
			http
			
			
			.csrf((csrf)->csrf
					
//					.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2/**"))
					.disable())
			.cors().and()
			.authorizeHttpRequests((authorize) ->
					authorize
					
					.requestMatchers("login").permitAll()

					.anyRequest()
					.authenticated()
//					.permitAll()
					
					)
			.addFilterAfter(new FilterAuthentication(),UsernamePasswordAuthenticationFilter.class)

			;
			
			
			 
					
					
	
			return http.build();
		}
//	 
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
//	 @Bean
//	 public FilterRegistrationBean corsFilter() {
//		 FilterRegistrationBean bean= new FilterRegistrationBean(new CorsFilter(corsConfigurationSource()));
//		 
//		 bean.setOrder(-102);
//		 return bean;
//		 
//	 
//	 }
	 
	 
}
