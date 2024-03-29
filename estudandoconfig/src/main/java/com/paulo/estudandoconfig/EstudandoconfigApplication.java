package com.paulo.estudandoconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.paulo.estudandoconfig.config.AppConfig;


@EnableAutoConfiguration
@SpringBootApplication
public class EstudandoconfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstudandoconfigApplication.class, args);
	}

}
