package com.paulo.estudandoconfig.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulo.estudandoconfig.dto.ChartDTO;
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
	@Test
	void findAllSuccessful() throws JsonProcessingException, Exception {
		SaleController controller = new SaleController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		String url = "/sales";
		SaleDTO dto = new SaleDTO().setOwner("carlos")
				.setProducts(List.of(new ProductSaleDTO().setName("tenis").setQuantity(30).setProductId(1L)));

		when(service.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(dto)));

		// Act
//		when(userMapper.toEntity(user)).

		 mockMvc.perform(get(url))
				.andExpect(jsonPath("$.content[0].owner")
			 			.value("carlos")).andExpect(status().isOk());

		
	}
	@Test
	void findAllByUsernameSuccessful() throws JsonProcessingException, Exception {
		SaleController controller = new SaleController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		String url = "/sales";
		SaleDTO dto = new SaleDTO().setOwner("carlos")
				.setProducts(List.of(new ProductSaleDTO().setName("tenis").setQuantity(30).setProductId(1L)));

		when(service.findAllByUsername(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(dto)));

		// Act

		 mockMvc.perform(get(url.concat("/username")))
				.andExpect(jsonPath("$.content[0].owner")
			 			.value("carlos")).andExpect(status().isOk());

		
	}
	
	@Test
	void deleteSuccessful() throws JsonProcessingException, Exception {
		SaleController controller = new SaleController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		String url = "/sales";
		
		when(service.deleteById(anyLong())).thenReturn(ResponseEntity.ok(""));

		// Act

		 mockMvc.perform(delete(url.concat("/3")))
				.andExpect(status().isOk());

		
	}
	@Test
	void chartSuccesful() throws JsonProcessingException, Exception {
		SaleController controller = new SaleController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		String url = "/sales";
		
		ChartDTO mock2 = mock(ChartDTO.class);
		when(service.saleChart(anyInt())).thenReturn(mock2);

		// Act

		 mockMvc.perform(get(url.concat("/chart")))
				.andExpect(status().isOk());

		
	}
	
	@Test
	void chartByUsernameSuccesful() throws JsonProcessingException, Exception {
		SaleController controller = new SaleController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		String url = "/sales";
		
		ChartDTO chartDTO = new ChartDTO(List.of(2,34,23,42,3,43,534),List.of(244,3554,2663,4233,223,4333,5234));
		
		when(service.saleChartByUser(anyInt(),anyString())).thenReturn(chartDTO);

		// Act

		 mockMvc.perform(get(url.concat("/chart/username")).param("username", "admin"))
				.andExpect(status().isOk());

		
	}
	
	@Test
	void monthlyIncomeByUsernameSuccesful() throws JsonProcessingException, Exception {
		SaleController controller = new SaleController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		String url = "/sales";
		
		
		when(service.incomeByUsername(anyInt(),anyString())).thenReturn(BigDecimal.valueOf(900));

		// Act

		 mockMvc.perform(get(url.concat("/income/user"))
				 .param("username", "admin")
		 		.param("month", "5"))
				.andExpect(status().isOk());

		
	}
	String toJson(Object o) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(o);
	}

}
