package com.paulo.estudandoconfig.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulo.estudandoconfig.dto.ProductSaleDTO;
import com.paulo.estudandoconfig.dto.SaleDTO;
import com.paulo.estudandoconfig.service.SaleService;
@WebMvcTest(controllers = SaleController.class)

class SaleControllerTest {
	@MockBean
	SaleService service;

	@Test
	void saveSuccessful() throws JsonProcessingException, Exception {
		SaleController controller = new SaleController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		String url = "/sales";
		SaleDTO dto = new SaleDTO().setOwner("carlos")
				.setProducts(List.of(new ProductSaleDTO().setName("tenis").setQuantity(30).setProductId(1L)));

		when(service.finishSale(dto)).thenReturn("ok");

		// Act
//		when(userMapper.toEntity(user)).

		MvcResult result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andReturn();
		verify(service).finishSale(dto);

		assertEquals(200, result.getResponse().getStatus());
		assertEquals("ok", result.getResponse().getContentAsString());
	}

	String toJson(Object o) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(o);
	}

}
