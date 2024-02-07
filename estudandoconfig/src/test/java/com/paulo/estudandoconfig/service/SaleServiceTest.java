package com.paulo.estudandoconfig.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paulo.estudandoconfig.dto.ChartDTO;
import com.paulo.estudandoconfig.dto.ProductSaleDTO;
import com.paulo.estudandoconfig.dto.SaleDTO;
import com.paulo.estudandoconfig.model.Product;
import com.paulo.estudandoconfig.model.ProductSale;
import com.paulo.estudandoconfig.model.Sale;
import com.paulo.estudandoconfig.repository.ProductRepository;
import com.paulo.estudandoconfig.repository.SaleRepository;

@ExtendWith(SpringExtension.class)

class SaleServiceTest {
	@Mock
	SaleRepository repository;

	@Mock
	ModelMapper mapper;

	@Mock
	ProductRepository productRepository;

	@InjectMocks
	SaleService service;

	SaleDTO dto = new SaleDTO();
	Sale sale = new Sale();

	@BeforeEach
	void setUp() {

		sale.setOwner("claudio").setId(1L)
		.setDate(LocalDateTime.now())
		.setProducts(List.of(new ProductSale()
						.setId(1L)
						.setName("calça")
						.setProductId(1L)
						.setQuantity(20)
						.setUnitPrice(BigDecimal.valueOf(900))));

		
		dto.setOwner("claudio").setId(1L).setDate(LocalDateTime.now())
				.setProducts(List.of(new ProductSaleDTO().setId(1L).setName("calça").setProductId(1L)));
	}

	@Test
	void finishSaleSuccessful() {
		List<String> roles = List.of("admin");
		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "",
				roles.stream().map(SimpleGrantedAuthority::new).toList());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		Product product = new Product().setId(1L).setQuantity(50).setName("calça").setPrice(BigDecimal.valueOf(90));

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(mapper.map(dto, Sale.class)).thenReturn(sale);
		String out = service.finishSale(dto);
		assertEquals("{}", out);

	}

	@Test
	void finishSaleRuntimeException() {
		List<String> roles = List.of("admin");
		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "",
				roles.stream().map(SimpleGrantedAuthority::new).toList());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		Product product = new Product().setId(1L).setQuantity(50).setName("calça").setPrice(BigDecimal.valueOf(90));
		sale.getProducts().get(0).setQuantity(90);
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(mapper.map(dto, Sale.class)).thenReturn(sale);
		assertThrows(RuntimeException.class, () -> service.finishSale(dto), "Not enough product on stock");

	}
	@Test
	void finishSaleRuntimeExceptionProductNotFound() {
		List<String> roles = List.of("admin");
		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "",
				roles.stream().map(SimpleGrantedAuthority::new).toList());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		Product product = new Product().setId(1L).setQuantity(50).setName("calça").setPrice(BigDecimal.valueOf(90));
		sale.getProducts().get(0).setQuantity(90);
		when(productRepository.findById(1L)).thenReturn(Optional.empty());
		when(mapper.map(dto, Sale.class)).thenReturn(sale);
		assertThrows(RuntimeException.class, () -> service.finishSale(dto), "The product doesnt exist.");

	}
	@Test
	void findAllSuccessful() {
		List<Sale> sales = List.of(sale.setOwner("claudio").setId(1L).setDate(LocalDateTime.now())
				.setProducts(List.of(new ProductSale().setId(1L).setName("calça").setProductId(1L).setQuantity(20)
						.setUnitPrice(BigDecimal.valueOf(900)))));

		when(mapper.map(dto, Sale.class)).thenReturn(sale);
		when(repository.findAll(any(PageRequest.class))).thenReturn(new PageImpl(sales));

		Page<SaleDTO> page = service.findAll(PageRequest.of(0, 5));

		assertEquals(sales.size(), page.getSize());

	}

	@Test
	void findAllByUsernameSuccessful() {
		List<String> roles = List.of("admin");
		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "",
				roles.stream().map(SimpleGrantedAuthority::new).toList());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		List<Sale> sales = List.of(sale.setOwner("claudio").setId(1L).setDate(LocalDateTime.now())
				.setProducts(List.of(new ProductSale().setId(1L).setName("calça").setProductId(1L).setQuantity(20)
						.setUnitPrice(BigDecimal.valueOf(900)))));

		when(mapper.map(sale, SaleDTO.class)).thenReturn(dto);
		when(repository.findAllByOwner(any(PageRequest.class), anyString())).thenReturn(new PageImpl(sales));

		Page<SaleDTO> page = service.findAllByUsername(PageRequest.of(0, 5));

		assertEquals(sales.size(), page.getSize());

	}

	@Test
	void deleteByIdStatusOk() {

		when(repository.existsById(anyLong())).thenReturn(true);
		ResponseEntity out = service.deleteById(1L);
		assertEquals(HttpStatus.OK, out.getStatusCode());
	}

	@Test
	void deleteByIdStatusNotFound() {

		when(repository.existsById(anyLong())).thenReturn(false);
		ResponseEntity out = service.deleteById(1L);
		assertEquals(HttpStatus.NOT_FOUND, out.getStatusCode());
		assertEquals("Entity not found", out.getBody());
	}

	@Test
	void saleChartAdmin() {
		List<int[]> y = new ArrayList();
		y.add(new int[] { 1, 500 });
		y.add(new int[] { 2, 300 });
		y.add(new int[] { 4, 500 });
		y.add(new int[] { 6, 700 });
		y.add(new int[] { 8, 500 });

		List<String> roles = List.of("admin");
		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "",
				roles.stream().map(SimpleGrantedAuthority::new).toList());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(repository.chartSaleMonth(anyInt())).thenReturn(y);

		ChartDTO out = service.saleChart(2015);
		assertEquals(5, out.xAxis().size());
		assertEquals(5, out.yAxis().size());

	}

	@Test
	void saleChartNotAdmin() {
		List<int[]> y = new ArrayList();
		y.add(new int[] { 1, 500 });
		y.add(new int[] { 2, 300 });
		y.add(new int[] { 4, 500 });
		y.add(new int[] { 6, 700 });
		y.add(new int[] { 8, 500 });

		List<String> roles = List.of("user");
		Authentication authentication = new UsernamePasswordAuthenticationToken("paulo", "",
				roles.stream().map(SimpleGrantedAuthority::new).toList());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(repository.chartSaleMonth(anyInt(), anyString())).thenReturn(y);

		ChartDTO out = service.saleChart(2015);
		assertEquals(5, out.xAxis().size());
		assertEquals(5, out.yAxis().size());

	}

	@Test
	void saleChartByUser() {
		List<int[]> y = new ArrayList<>();
		y.add(new int[] { 1, 500 });
		y.add(new int[] { 2, 300 });
		y.add(new int[] { 4, 500 });
		y.add(new int[] { 6, 700 });
		y.add(new int[] { 8, 500 });
		when(repository.chartSaleMonth(anyInt(), anyString())).thenReturn(y);
		
		ChartDTO out = service.saleChartByUser(2015,"nome");
		assertEquals(5, out.xAxis().size());
		assertEquals(5, out.yAxis().size());
	}
	
	@Test
	void incomeByUsernameSuccessful() {
		when(repository.totalIncome(anyInt(), anyString())).thenReturn(BigDecimal.valueOf(5400));
		BigDecimal out = service.incomeByUsername(5, "paulo");
		assertEquals(BigDecimal.valueOf(5400), out);
		
		
	}

}
