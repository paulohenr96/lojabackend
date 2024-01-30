/**
 * 
 */
package com.paulo.estudandoconfig.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
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

import com.paulo.estudandoconfig.dto.InfoDTO;
import com.paulo.estudandoconfig.dto.ProductDTO;
import com.paulo.estudandoconfig.model.Product;
import com.paulo.estudandoconfig.repository.ProductRepository;
import com.paulo.estudandoconfig.repository.SaleRepository;

/**
 * @author paulo
 *
 */

@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {AppConfig.class})
public class ProductServiceTest {

	@Mock
	ProductRepository repository;

	@Mock
	SaleRepository saleRepository;

	@Mock
	ModelMapper mapper;

	@InjectMocks
	ProductService service;

	Product product = new Product();
	ProductDTO productDTO = new ProductDTO();

	@BeforeEach
	void setUp() throws Exception {
		product.setId(1L);
		product.setCategory("clothes");
		product.setName("socks");
		product.setPrice(BigDecimal.valueOf(95.0));
		product.setQuantity(900);

		productDTO.setId(1L);
		productDTO.setCategory("clothes");
		productDTO.setName("socks");
		productDTO.setPrice(BigDecimal.valueOf(95.0));
		productDTO.setQuantity(900);
//		
//		when(mapper.map(product, ProductDTO.class)).thenReturn(productDTO);
//		when(mapper.map(productDTO, Product.class)).thenReturn(product);
//		

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void saveSuccesful() {
		when(mapper.map(productDTO, Product.class)).thenReturn(product);

		String save = service.save(productDTO);

		product.setId(null);

		assertEquals("", save);
		verify(repository, times(1)).save(product);

	}

	@Test
	void getAllSuccessful() {
		int pageSize = 10;
		PageRequest pageRequest = PageRequest.of(0, pageSize);

		Page<ProductDTO> mockDTO = Mockito.mock(Page.class);
		Page<Product> mock = Mockito.mock(Page.class);

		when(repository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(generateListOfProducts()));
		when(mapper.map(product, ProductDTO.class)).thenReturn(productDTO);

		Page<ProductDTO> output = service.getAll(pageRequest);

		assertSame(5, output.getContent().size());

	}

	@Test
	void updateByIdSuccessful() {
		Long id = 1L;

		when(repository.findById(anyLong())).thenReturn(Optional.of(product));
		when(repository.save(product)).thenReturn((product));
		when(mapper.map(product, ProductDTO.class)).thenReturn(productDTO);

		ProductDTO out = service.updateById(productDTO, id);

		assertEquals(productDTO, out);

	}

	@Test
	void updateByIdNotFound() {
		Long id = 1L;

		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		ProductDTO out = service.updateById(productDTO, id);

		assertEquals(null, out);

	}

	@Test
	void findByIdSuccesful() {
		when(mapper.map(product, ProductDTO.class)).thenReturn(productDTO);
		when(repository.findById(anyLong())).thenReturn(Optional.of(product));

		ProductDTO out = service.findById(1L);

		assertEquals(productDTO.getId(), out.getId());
	}

	@Test
	void checkQuantityEmptyList() {
		when(repository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(generateListOfProducts()));
		when(mapper.map(product, ProductDTO.class)).thenReturn(productDTO);

		List<ProductDTO> out = service.checkQuantity();

		assertTrue(out.isEmpty());
		;
	}

	@Test
	void checkQuantityNotEmptyList() {
		when(repository.findAll()).thenReturn(

				List.of(new Product("JORDAN", 3, BigDecimal.valueOf(100)),
						new Product("KOBE", 4, BigDecimal.valueOf(80)),
						new Product("Lupo", 230, BigDecimal.valueOf(10)),
						new Product("Adidas", 750, BigDecimal.valueOf(120)),
						new Product("Nike", 300, BigDecimal.valueOf(100))

				)

		);
		when(mapper.map(product, ProductDTO.class)).thenReturn(productDTO);

		List<ProductDTO> out = service.checkQuantity();

		assertTrue(out.size() == 2);
		;
	}

	@Test
	void deleteByIdStatusOk() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(product));

		ResponseEntity out = service.deleteById(1L);

		assertEquals(HttpStatus.OK, out.getStatusCode());
	}

	@Test
	void deleteByIdStatusNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());

		ResponseEntity out = service.deleteById(1L);

		assertEquals(HttpStatus.NOT_FOUND, out.getStatusCode());
	}

	@Test
	void infoAdmin() {
		List<String> roles = List.of("admin");
		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "", roles.stream().map(SimpleGrantedAuthority::new).toList());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(repository.sumQuantity()).thenReturn(20);
		when(saleRepository.totalIncome(anyInt())).thenReturn(BigDecimal.valueOf(900));

		InfoDTO out = service.info();
		assertEquals(BigDecimal.valueOf(900), out.income());
		verify(saleRepository).totalIncome(LocalDate.now().getMonthValue());

	}
	@Test
	void infoNotAdmin() {
		List<String> roles = List.of("user");
		Authentication authentication = new UsernamePasswordAuthenticationToken("username", "", roles.stream().map(SimpleGrantedAuthority::new).toList());
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		when(repository.sumQuantity()).thenReturn(20);
		when(saleRepository.totalIncome(anyInt(),anyString())).thenReturn(BigDecimal.valueOf(900));

		InfoDTO out = service.info();
		assertEquals(BigDecimal.valueOf(900), out.income());
		verify(saleRepository).totalIncome(LocalDate.now().getMonthValue(),"username");
		

	}

	List<Product> generateListOfProducts() {
		return List.of(new Product("JORDAN", 300, BigDecimal.valueOf(100)),
				new Product("KOBE", 30, BigDecimal.valueOf(80)), new Product("Lupo", 230, BigDecimal.valueOf(10)),
				new Product("Adidas", 750, BigDecimal.valueOf(120)), new Product("Nike", 300, BigDecimal.valueOf(100))

		);
	}

}
