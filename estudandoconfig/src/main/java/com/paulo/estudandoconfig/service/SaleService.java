package com.paulo.estudandoconfig.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
		sale.setOwner(username());
		sale.getProducts().forEach(this::updateQuantity);
		sale.calculateTotal();
		sale.setDate(LocalDateTime.now());
		repository.save(sale);
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
		return repository.findAll(of).map(this::toDTO);
	}

	public Page<SaleDTO> findAllByUsername(PageRequest of) {
		return repository.findAllByOwner(of, username()).map(this::toDTO);
	}

	public String deleteById(Long id) {
		repository.deleteById(id);
		return "{}";
	}

	private boolean isAdmin() {
		return SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities().stream()
				.filter(e -> e.getAuthority().equals("admin")).findFirst().isPresent();
	}

	private String username() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public ChartDTO saleChart(Integer year) {
		return isAdmin() ? buildChart(repository.chartSaleMonth(year)) : saleChartByUser(year, username());

	}

	public ChartDTO saleChartByUser(Integer year, String username) {
		return buildChart(repository.chartSaleMonth(year, username));
	}

	public BigDecimal incomeByUsername(Integer month, String username) {
		return repository.totalIncome(month, username);

	}
	private SaleDTO toDTO(Sale sale) {
		return  mapper.map(sale, SaleDTO.class);
	}
	private ChartDTO buildChart(List<int[]> arr) {
		return new ChartDTO(arr.stream().map(e -> e[0]).toList(), arr.stream().map(e -> e[1]).toList());
	}
}
