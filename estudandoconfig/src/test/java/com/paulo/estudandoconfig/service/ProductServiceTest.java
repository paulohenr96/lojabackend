/**
 * 
 */
package com.paulo.estudandoconfig.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
		verify(repository,times(1)).save(product);
		
	}

}
