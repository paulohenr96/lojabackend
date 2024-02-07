package com.paulo.estudandoconfig.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulo.estudandoconfig.dto.InfoDTO;
import com.paulo.estudandoconfig.dto.ProductDTO;
import com.paulo.estudandoconfig.exception.ExcentionController;
import com.paulo.estudandoconfig.service.ProductService;

@WebMvcTest(controllers = ProductController.class)

class ProductControllerTest {
	@MockBean
	ProductService service;

	@Test
	void saveProductValidator() throws JsonProcessingException, Exception {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");

		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);

		ProductController controller = new ProductController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new ExcentionController())
				.setValidator(bean) // Configurar o validador aqui
				.build();

		String url = "/products";

		ProductDTO dto = new ProductDTO().setPrice(BigDecimal.valueOf(-9000)).setQuantity(-90);
		when(service.save(dto)).thenReturn("");

		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").value(Matchers.hasItem("Please insert the category of the product")))
				.andExpect(jsonPath("$")
						.value(Matchers.hasItem("The quantity must me a number equal or greater than zero")))
				.andExpect(
						jsonPath("$").value(Matchers.hasItem("The price must me a number equal or greater than zero")))
				.andExpect(jsonPath("$").value(Matchers.hasItem("Please insert the name of the product")))
				.andExpect(status().isBadRequest());

	}

	@Test
	void saveProductSuccessful() throws JsonProcessingException, Exception {
		ProductController controller = new ProductController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		String url = "/products";

		ProductDTO dto = new ProductDTO().setName("Socks").setPrice(BigDecimal.valueOf(9000)).setCategory("clothes")
				.setQuantity(90);
		when(service.save(dto)).thenReturn("");

		// Act
//		when(userMapper.toEntity(user)).

		MvcResult result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andReturn();
		assertEquals(200, result.getResponse().getStatus());
		assertEquals("", result.getResponse().getContentAsString());

	}

	@Test
	void getAllSuccessful() throws JsonProcessingException, Exception {
		ProductController controller = new ProductController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		String url = "/products";

		ProductDTO dto = new ProductDTO().setName("Socks").setPrice(BigDecimal.valueOf(9000)).setCategory("clothe")
				.setQuantity(90);

		when(service.getAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(dto)));

		// Act
//		when(userMapper.toEntity(user)).

		mockMvc.perform(get(url).param("page", "0")).andExpect(jsonPath("$.content[0].name").value("Socks"))
				.andExpect(status().isOk());

	}

	@Test
	void getAllNegativePage() throws JsonProcessingException, Exception {
		ProductController controller = new ProductController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new ExcentionController())
				.setValidator(new LocalValidatorFactoryBean()).build();
		String url = "/products";

		ProductDTO dto = new ProductDTO().setName("Socks").setPrice(BigDecimal.valueOf(9000)).setCategory("clothe")
				.setQuantity(90);

		when(service.getAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(dto)));

		// Act
//		when(userMapper.toEntity(user)).
		MvcResult result = mockMvc.perform(get(url).param("page", "-1")).andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

	}

	@Test
	void getAllByCategorySuccessful() throws JsonProcessingException, Exception {
		ProductController controller = new ProductController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		String url = "/products";
		String category = "clothes";
		ProductDTO dto = new ProductDTO().setName("Socks").setPrice(BigDecimal.valueOf(9000)).setCategory("clothes")
				.setQuantity(90);

		when(service.getAll(any(PageRequest.class), anyString())).thenReturn(new PageImpl<>(List.of(dto)));

		// Act
//		when(userMapper.toEntity(user)).

		mockMvc.perform(get(url.concat("/category/".concat(category))))
				.andExpect(jsonPath("$.content[0].name").value("Socks"))
				.andExpect(jsonPath("$.content[0].category").value(category)).andExpect(status().isOk());

	}

	@Test
	void updateSuccessful() throws JsonProcessingException, Exception {
		ProductController controller = new ProductController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		String url = "/products";

		Long id = 1L;
		String category = "clothes";

		ProductDTO dto = new ProductDTO().setName("Socks")

				.setPrice(BigDecimal.valueOf(9000)).setCategory("clothes").setQuantity(90);

		when(service.updateById(any(ProductDTO.class), anyLong())).thenReturn(dto);

		mockMvc.perform(put(url + "/" + id).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))

				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Socks"));

	}

	@Test
	void deteleSuccessful() throws JsonProcessingException, Exception {
		ProductController controller = new ProductController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		String url = "/products";

		Long id = 1L;
		String category = "clothes";

		ProductDTO dto = new ProductDTO().setName("Socks")

				.setPrice(BigDecimal.valueOf(9000)).setCategory("clothes").setQuantity(90);

		when(service.deleteById(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(delete(url + "/" + id))

				.andExpect(status().isOk());

	}

	@Test
	void findByIdSuccessful() throws JsonProcessingException, Exception {
		ProductController controller = new ProductController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		String url = "/products";

		Long id = 1L;
		String category = "clothes";

		ProductDTO dto = new ProductDTO().setName("Socks")

				.setPrice(BigDecimal.valueOf(9000)).setCategory("clothes").setQuantity(90);

		when(service.findById(anyLong())).thenReturn(dto);

		mockMvc.perform(get(url + "/" + id))

				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Socks"));

	}

	@Test
	void countProductSuccessful() throws JsonProcessingException, Exception {
		ProductController controller = new ProductController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		String url = "/products";

		Long id = 1L;
		String category = "clothes";

		ProductDTO dto = new ProductDTO().setName("Socks")

				.setPrice(BigDecimal.valueOf(9000)).setCategory("clothes").setQuantity(90);
		;
		when(service.info()).thenReturn(new InfoDTO(BigDecimal.valueOf(400), 500L, 50));

		mockMvc.perform(get(url + "/infos"))

				.andExpect(status().isOk()).andExpect(jsonPath("$.income").value("400"));

	}

	@Test
	void checkQuantitySuccessful() throws JsonProcessingException, Exception {
		ProductController controller = new ProductController(service);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		String url = "/products";

		Long id = 1L;
		String category = "clothes";

		ProductDTO dto = new ProductDTO().setName("Socks")

				.setPrice(BigDecimal.valueOf(9000)).setCategory("clothes").setQuantity(90);
		;
		when(service.checkQuantity()).thenReturn(List.of(dto));

		mockMvc.perform(get(url + "/checkquantity"))

				.andExpect(status().isOk()).andExpect(jsonPath("$.[0].name").value("Socks"));

	}

	String toJson(Object o) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(o);
	}

}
