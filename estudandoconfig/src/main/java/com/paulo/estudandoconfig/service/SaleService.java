package com.paulo.estudandoconfig.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import com.paulo.estudandoconfig.dto.ChartDTO;
import com.paulo.estudandoconfig.dto.SaleDTO;
import com.paulo.estudandoconfig.model.Product;
import com.paulo.estudandoconfig.model.ProductSale;
import com.paulo.estudandoconfig.model.Sale;
import com.paulo.estudandoconfig.repository.ProductRepository;
import com.paulo.estudandoconfig.repository.SaleRepository;

public class SaleService {

	@Autowired
	private SaleRepository repository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductRepository productRepository;

	public String finishSale(SaleDTO dto) {

		Sale sale = mapper.map(dto, Sale.class);

		sale.getProducts().forEach(e -> e.setProduct(productRepository.findById(e.getProduct().getId()).get()));

		BigDecimal totalPrice = sale.getProducts().stream()
				.map(e -> e.getProduct().getPrice().multiply(BigDecimal.valueOf(e.getQuantity())))
				.reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

		sale.setOwner(SecurityContextHolder.getContext().getAuthentication().getName());
		
		sale.getProducts().forEach(this::updateQuantity);
		sale.setTotalPrice(totalPrice);
//		sale.setDate(LocalDateTime.now());

		Sale save = repository.save(sale);
		return "{}";
	}

	public void updateQuantity(ProductSale p) {
		Product product = p.getProduct();
		if (product.getQuantity() < p.getQuantity()) {
			throw new RuntimeException("Not enough product on stock");
		}

		product.setQuantity(product.getQuantity() - p.getQuantity());

		productRepository.save(product);
	}

	public Page<SaleDTO> findAll(PageRequest of) {
		// TODO Auto-generated method stub
		return repository.findAll(of).map(e -> mapper.map(e, SaleDTO.class));
	}

	public String deleteById(Long id) {
		// TODO Auto-generated method stub

		repository.deleteById(id);

		return "{}";
	}

	public ChartDTO saleChart(Integer year) {

		List<int[]> chart = repository.chartSaleMonth(year);

		return new ChartDTO(chart.stream().map(e -> e[0]).toList(), chart.stream().map(e -> e[1]).toList());

	}

}
