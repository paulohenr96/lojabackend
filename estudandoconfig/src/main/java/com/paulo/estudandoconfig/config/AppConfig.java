package com.paulo.estudandoconfig.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.paulo.estudandoconfig.JWTCreator;
import com.paulo.estudandoconfig.service.SaleService;

import jakarta.validation.Validator;

@Configuration
public class AppConfig {

//	
//	@Bean
//	public ProductService productService() {
//		return new ProductService();
//	}
//	
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

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");

		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}

}
