package com.paulo.estudandoconfig.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulo.estudandoconfig.dto.UserAccountDTO;
import com.paulo.estudandoconfig.mapper.UserAccountMapper;
import com.paulo.estudandoconfig.model.Role;
import com.paulo.estudandoconfig.model.UserAccount;
import com.paulo.estudandoconfig.repository.MetricRepository;
import com.paulo.estudandoconfig.repository.UserAccountRepository;
@WebMvcTest(controllers = UserController.class)

class UserControllerTest {
	@MockBean
	UserAccountRepository repository;
	@MockBean
	 MetricRepository metricsRepository;

	@MockBean
	 UserAccountMapper userMapper;
	
	String url="/users";
	@Test
	void saveSuccessful() throws Exception {
		UserController controller = new UserController(repository,metricsRepository,userMapper);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		
		
		UserAccountDTO dto= new UserAccountDTO()
				.setUserName("admin")
				.setRolesName(List.of("admin"))
				.setPassword(("123"));
		
		UserAccount user= new UserAccount()
				.setUserName("admin")
				.setRoles(Set.of(new Role().setName("admin")))
				.setPassword(("123"));
		when(userMapper.toEntity(any(UserAccountDTO.class))).thenReturn(user);
		when(repository.findByUserName(anyString())).thenReturn(Optional.empty());
		
		// Act
//		when(userMapper.toEntity(user)).
		
		MvcResult result =  mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(toJson(dto))).andReturn();
		assertEquals(200, result.getResponse().getStatus());
		assertEquals("", result.getResponse().getContentAsString());
	}
	@Test
	void saveInvalidUsername() throws Exception {
		UserController controller = new UserController(repository,metricsRepository,userMapper);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		
		
		UserAccountDTO dto= new UserAccountDTO()
				.setUserName("admin")
				.setRolesName(List.of("admin"))
				.setPassword(("123"));
		
		UserAccount user= new UserAccount()
				.setUserName("admin")
				.setRoles(Set.of(new Role().setName("admin")))
				.setPassword(("123"));
		when(userMapper.toEntity(any(UserAccountDTO.class))).thenReturn(user);
		when(repository.findByUserName(anyString())).thenReturn(Optional.of(user));
		
		// Act
//		when(userMapper.toEntity(user)).
		
		MvcResult result =  mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(toJson(dto))).andReturn();
		assertEquals(200, result.getResponse().getStatus());
		assertEquals("Invalid Username", result.getResponse().getContentAsString());
	}
	
	
	String toJson(Object o) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(o);
	}
	
}
