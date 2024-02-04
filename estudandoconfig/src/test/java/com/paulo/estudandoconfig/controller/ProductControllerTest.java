package com.paulo.estudandoconfig.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paulo.estudandoconfig.service.ProductService;
import com.paulo.estudandoconfig.service.SaleService;
@WebMvcTest(controllers = SaleController.class)
class ProductControllerTest {
	@MockBean
	ProductService service;

	@Test
	void saveProductSuccessful() {

		
	}

}
