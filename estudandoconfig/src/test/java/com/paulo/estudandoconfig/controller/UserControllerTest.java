package com.paulo.estudandoconfig.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulo.estudandoconfig.dto.UserAccountDTO;
import com.paulo.estudandoconfig.exception.ExcentionController;
import com.paulo.estudandoconfig.mapper.UserAccountMapper;
import com.paulo.estudandoconfig.model.Metrics;
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

	String url = "/users";
	MockMvc mockMvc;
	@BeforeEach
	void setUp() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");

		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		UserController controller = new UserController(repository, metricsRepository, userMapper);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new ExcentionController())
				.setValidator(bean).build();

	}
	@Test
	void saveSuccessful() throws Exception {
	

		UserAccountDTO dto = new UserAccountDTO().setUserName("admin").setRolesName(List.of("admin"))
				.setPassword(("123")).setName("paulo");

		UserAccount user = new UserAccount().setUserName("admin").setRoles(Set.of(new Role().setName("admin")))
				.setPassword(("123"));
		when(userMapper.toEntity(any(UserAccountDTO.class))).thenReturn(user);
		when(repository.findByUserName(anyString())).thenReturn(Optional.empty());

		MvcResult result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andReturn();
		verify(repository).save(user);

		assertEquals(200, result.getResponse().getStatus());
		assertEquals("", result.getResponse().getContentAsString());
	}
	@Test
	void saveInvalidRole() throws Exception {
	

		UserAccountDTO dto = new UserAccountDTO().setUserName("admin").setRolesName(List.of("zxczxczxczx"))
				.setPassword(("123")).setName("paulo");

		UserAccount user = new UserAccount().setUserName("bvnvbnvbnvb").setRoles(Set.of(new Role().setName("admin")))
				.setPassword(("123"));
		when(userMapper.toEntity(any(UserAccountDTO.class))).thenReturn(user);
		when(repository.findByUserName(anyString())).thenReturn(Optional.empty());

		MvcResult result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andReturn();

		assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
		assertEquals("The role zxczxczxczx isnt valid.", result.getResponse().getContentAsString());

	}
	
	@Test
	void saveValidator() throws Exception {

		UserAccountDTO dto = new UserAccountDTO().setUserName("").setRolesName(List.of("admin")).setMonthlyGoal(BigDecimal.valueOf(-99))
				.setPassword((""));

		UserAccount user = new UserAccount().setUserName("admin").setRoles(Set.of(new Role().setName("admin")))
				.setPassword(("123"));
		when(userMapper.toEntity(any(UserAccountDTO.class))).thenReturn(user);
		when(repository.findByUserName(anyString())).thenReturn(Optional.empty());

		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
		.andExpect(jsonPath("$").isArray())
		.andExpect(jsonPath("$").value(Matchers.hasItem("Please insert the username")))
		.andExpect(jsonPath("$").value(Matchers.hasItem("Please insert the password")))
		.andExpect(jsonPath("$").value(Matchers.hasItem("Please insert the name")))
		.andExpect(jsonPath("$").value(Matchers.hasItem("The monthly goal must me a number equal or greater than zero")))
		.andExpect(status().isBadRequest());
		
		
		
		
		

		
	}

	@Test
	void saveInvalidUsername() throws Exception {

		UserAccountDTO dto = new UserAccountDTO().setUserName("admin").setRolesName(List.of("admin"))
				.setPassword(("123")).setName("Paulo");

		UserAccount user = new UserAccount().setUserName("admin").setRoles(Set.of(new Role().setName("admin")))
				.setPassword(("123"));
		when(userMapper.toEntity(any(UserAccountDTO.class))).thenReturn(user);
		when(repository.findByUserName(anyString())).thenReturn(Optional.of(user));

		// Act
//		when(userMapper.toEntity(user)).

		MvcResult result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andReturn();

		assertEquals(200, result.getResponse().getStatus());
		assertEquals("Invalid Username", result.getResponse().getContentAsString());
	}

	@Test
	void editSuccessful() throws Exception {

		UserAccountDTO dto = new UserAccountDTO().setUserName("admin").setName("Claudio").setRolesName(List.of("admin"))
				.setId(1L).setPassword(("123"));

		UserAccount user = new UserAccount().setUserName("admin").setId(1L).setName("Pedro")
				.setRoles(Set.of(new Role().setName("admin"))).setPassword(("123"));
		when(userMapper.toEntity(any(UserAccountDTO.class))).thenReturn(user);
		when(repository.findById(anyLong())).thenReturn(Optional.of(user));
		when(userMapper.toEntity(any(UserAccountDTO.class))).thenReturn(user.setName("claudio"));

		// Act
//		when(userMapper.toEntity(user)).

		MvcResult result = mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andReturn();

		assertEquals(200, result.getResponse().getStatus());
		verify(repository).save(user.setName("claudio"));
	}

	@Test
	void editStatusConflict() throws Exception {

		UserAccountDTO dto = new UserAccountDTO().setUserName("arc").setName("Claudio").setRolesName(List.of("admin"))
				.setId(1L).setPassword(("123"));

		UserAccount user = new UserAccount().setUserName("admin").setId(1L).setName("Pedro")
				.setRoles(Set.of(new Role().setName("admin"))).setPassword(("123"));
		UserAccount user2 = new UserAccount().setUserName("arc").setId(2L).setName("Pedro")
				.setRoles(Set.of(new Role().setName("admin"))).setPassword(("123"));

		when(repository.findById(anyLong())).thenReturn(Optional.of(user));
		when(repository.findByUserName(anyString())).thenReturn(Optional.of(user2));

		MvcResult result = mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andReturn();

		assertEquals(HttpStatus.CONFLICT.value(), result.getResponse().getStatus());
	}

	@Test
	void editStatusNotFound() throws Exception {

		UserAccountDTO dto = new UserAccountDTO().setUserName("arc").setName("Claudio").setRolesName(List.of("admin"))
				.setId(1L).setPassword(("123"));

		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		MvcResult result = mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andReturn();

		assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}

	@Test
	void newPasswordStatusOk() throws Exception {

		UserAccountDTO dto = new UserAccountDTO().setUserName("arc").setName("Claudio").setRolesName(List.of("admin"))
				.setId(1L).setPassword(("123"));

		UserAccount user = new UserAccount().setUserName("admin").setId(1L).setName("Pedro")
				.setRoles(Set.of(new Role().setName("admin"))).setPassword(("123"));

		when(repository.findById(anyLong())).thenReturn(Optional.of(user));

		MvcResult result = mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andReturn();

		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}
	

	@Test
	void newPasswordStatusNotFound() throws Exception {

		UserAccountDTO dto = new UserAccountDTO().setUserName("arc").setName("Claudio").setRolesName(List.of("admin"))
				.setId(1L).setPassword(("123"));

		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		MvcResult result = mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(dto)))
				.andReturn();

		assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}

	@Test
	void getAllStatusOkList() throws Exception {

		UserAccountDTO dto = new UserAccountDTO().setUserName("arc").setName("Claudio").setRolesName(List.of("admin"))
				.setId(1L).setPassword(("123"));

		UserAccount user = new UserAccount().setUserName("admin").setId(1L).setName("Pedro")
				.setRoles(Set.of(new Role().setName("admin"))).setPassword(("123"));

		when(userMapper.toDTO(any(UserAccount.class))).thenReturn(dto);
		when(repository.findAll()).thenReturn(List.of(user));

		 mockMvc.perform(get(url))
		 	.andExpect(jsonPath("$")
		 			.isArray()).andExpect(status().isOk())
		 	.andExpect(jsonPath("$.length()").value(1))
		 	.andExpect(jsonPath("$[0].name").value("Claudio"));
		 			
		 	
		
	}
	
	
	@Test
	void getByIdStatusOk() throws Exception {

		Long id=1L;
		
		UserAccountDTO dto = new UserAccountDTO()
				.setUserName("arc")
				.setName("Claudio")
				.setRolesName(List.of("admin"))
				.setId(1L).setPassword(("123"));

		UserAccount user = new UserAccount()
				.setUserName("admin")
				.setId(1L).setName("Pedro")
				.setRoles(Set.of(new Role()
						.setName("admin")))
				.setPassword(("123"));

		when(userMapper.toDTO(any(UserAccount.class))).thenReturn(dto);
		when(repository.findById(anyLong())).thenReturn(Optional.of(user));

		 mockMvc.perform(get(url.concat("/").concat(id.toString())))
		 	.andExpect(jsonPath("$.name")
		 			.value("Claudio")).andExpect(status().isOk()) 	;
		 			
		 	
		
	}
	@Test
	void getByIdStatusNotFound() throws Exception {

		Long id=1L;
		
		UserAccountDTO dto = new UserAccountDTO().setUserName("arc").setName("Claudio").setRolesName(List.of("admin"))
				.setId(1L).setPassword(("123"));

		UserAccount user = new UserAccount().setUserName("admin").setId(1L).setName("Pedro")
				.setRoles(Set.of(new Role().setName("admin"))).setPassword(("123"));

		when(userMapper.toDTO(any(UserAccount.class))).thenReturn(dto);
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		 mockMvc.perform(get(url.concat("/").concat(id.toString())))
		 	.andExpect(status().is(HttpStatus.NOT_FOUND.value())) 	;
		 			
		 	
		
	}
	
	@Test
	void deleteByIdStatusOk() throws Exception {

		Long id=1L;
		
	

		when(repository.existsById(anyLong())).thenReturn(true);

		 mockMvc.perform(delete(url.concat("/").concat(id.toString())))
		 	.andExpect(status().is(HttpStatus.OK.value())) 	;
		 			
		 	
		
	}
	@Test
	void deleteByIdStatusNotFound() throws Exception {

		Long id=1L;
		
	

		when(repository.existsById(anyLong())).thenReturn(false);

		 mockMvc.perform(delete(url.concat("/").concat(id.toString())))
		 	.andExpect(status().is(HttpStatus.NOT_FOUND.value())) 	;
		 			
		 	
		
	}
	
	@Test
	void metricsStatusOkHaveMetrics() throws Exception {

		Long id=1L;
		
	
		Metrics m = new Metrics().setId(1L).setMonthlyGoal(BigDecimal.valueOf(900));
		when(metricsRepository.findAll()).thenReturn(List.of(m));

		 mockMvc.perform(get(url.concat("/metrics")))
		 	
		 	.andExpect(jsonPath("$.id")
		 			.value("1"))
		 	.andExpect(jsonPath("$.monthlyGoal")
		 			.value("900"))
		 	.andExpect(status().is(HttpStatus.OK.value())) 	;
		 			
		 	
		
	}
	
	@Test
	void metricsStatusOkNewMetrics() throws Exception {

		Long id=1L;
		
	
		Metrics m = new Metrics().setId(1L).setMonthlyGoal(BigDecimal.valueOf(900));
		when(metricsRepository.findAll()).thenReturn(List.of());

		 mockMvc.perform(get(url.concat("/metrics")))
		 	
		 	
		 	.andExpect(jsonPath("$.monthlyGoal")
		 			.value("0"))
		 	.andExpect(status().is(HttpStatus.OK.value())) 	;
		 			
		 	
		
	}

	@Test
	void confirmPasswordStatusOk() throws Exception {
		List<String> roles = List.of("admin");
		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "",
				roles.stream().map(SimpleGrantedAuthority::new).toList());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		

		Long id=1L;
		
	
		UserAccount account = new UserAccount().setPassword(new BCryptPasswordEncoder().encode("123"));
			
		when(repository.findByUserName(anyString())).thenReturn(Optional.of(account));
		
		 mockMvc.perform(post(url.concat("/confirmpassword")).content("123"))
		 	
		 	
		 	
		 	.andExpect(status().is(HttpStatus.OK.value())) 	;
		 			
		 	
		
	}
	@Test
	void confirmPasswordStatusUnauthorized() throws Exception {
		List<String> roles = List.of("admin");
		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "",
				roles.stream().map(SimpleGrantedAuthority::new).toList());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		

		Long id=1L;
		
		final String WRONG_PASSWORD="wrong";
		UserAccount account = new UserAccount().setPassword(new BCryptPasswordEncoder().encode("123"));
			
		when(repository.findByUserName(anyString())).thenReturn(Optional.of(account));
		
		 mockMvc.perform(post(url.concat("/confirmpassword")).content(WRONG_PASSWORD))
		 	.andExpect(status().is(HttpStatus.UNAUTHORIZED.value())) 	;
		 			
		 	
		
	}
	@Test
	void editMetricsStatusOk() throws Exception{

		Long id=1L;
		
		Metrics metrics = new Metrics().setId(1L).setMonthlyGoal(BigDecimal.valueOf(1000));
		
		 mockMvc.perform(put(url.concat("/metrics")).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(metrics)))
		 	.andExpect(status().is(HttpStatus.OK.value()));
	}
	
	@Test
	void editMetricsValidator() throws Exception{

		Long id=1L;
		
		Metrics metrics = new Metrics().setId(1L).setMonthlyGoal(BigDecimal.valueOf(-1000));
		
		 mockMvc.perform(put(url.concat("/metrics")).contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(metrics)))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$").value(Matchers.hasItem("The monthly goal should be equal or greater than zero")))
			.andExpect(status().isBadRequest());
	}
	
	
	
	
	String toJson(Object o) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(o);
	}

}
