package com.paulo.estudandoconfig.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paulo.estudandoconfig.JWTCreator;
import com.paulo.estudandoconfig.repository.SaleRepository;
import com.paulo.estudandoconfig.service.ProductService;
import com.paulo.estudandoconfig.service.SaleService;

@Configuration
public class AppConfig {

	
	@Bean
	public ProductService productService() {
		return new ProductService();
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public SaleService saleService() {
		return new SaleService();
	}
	@Bean
	public JWTCreator JWTCreator() {
		return new JWTCreator();
	}
	
	
	
	
}
