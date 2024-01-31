package com.paulo.estudandoconfig.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulo.estudandoconfig.dto.LoginDTO;
import com.paulo.estudandoconfig.model.Role;
import com.paulo.estudandoconfig.model.UserAccount;
import com.paulo.estudandoconfig.repository.UserAccountRepository;
@WebMvcTest(controllers = LoginController.class)
class LoginControllerTest {
	
//    @Autowired
//    private MockMvc mockMvc;
    
	@MockBean
	UserAccountRepository repository;
	

	
	@Test
	void loginSuccesful() throws Exception {
		LoginController controller = new LoginController(repository);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		
		
		UserAccount user= new UserAccount()
				.setId(1L)
				.setUserName("admin")
				.setRoles(Set.of(new Role().setName("admin")))
				.setPassword(new BCryptPasswordEncoder()
						.encode("123"));
		when(repository.findByUserName(anyString())).thenReturn(Optional.of(user));
		
		// Act
		LoginDTO login=new LoginDTO()
				.setPassword("123")
				.setUsername("admin");
		
		MvcResult result =  mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(toJson(login))).andReturn();
		verify(repository).findByUserName("admin");
		assertEquals(200, result.getResponse().getStatus());
	}
	
	
	
	@Test
	void loginUserNotFoundStatusUnnathorized() throws Exception {
		LoginController controller = new LoginController(repository);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		
		
		UserAccount user= new UserAccount()
				.setId(1L)
				.setUserName("admin")
				.setRoles(Set.of(new Role().setName("admin")))
				.setPassword(new BCryptPasswordEncoder()
						.encode("23423423423"));
		when(repository.findByUserName(anyString())).thenReturn(Optional.of(user));
		
		// Act
		LoginDTO login=new LoginDTO()
				.setPassword("123")
				.setUsername("admin");
		
		MvcResult result =  mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(toJson(login))).andReturn();
		verify(repository).findByUserName("admin");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
	}
	
	@Test
	void loginUserWrongPasswordUnnathorized() throws Exception {
		LoginController controller = new LoginController(repository);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		
		
		when(repository.findByUserName(anyString())).thenReturn(Optional.empty());
		
		// Act
		LoginDTO login=new LoginDTO()
				.setPassword("123")
				.setUsername("admin");
		
		MvcResult result =  mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(toJson(login))).andReturn();
		verify(repository).findByUserName("admin");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
	}
	
	@Test
	void logoutSuccessful() throws Exception {
		LoginController controller = new LoginController(repository);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		
		
		
		
		// Act
		
		
		MvcResult result =  mockMvc.perform(get("/logout")).andReturn();
		assertEquals(200, result.getResponse().getStatus());
	}
	
	
	
	
	
	String toJson(Object o) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(o);
	}
	
}
