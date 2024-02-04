package com.paulo.estudandoconfig.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.paulo.estudandoconfig.context.ContextHolder;
import com.paulo.estudandoconfig.dto.ChartDTO;
import com.paulo.estudandoconfig.dto.SaleDTO;
import com.paulo.estudandoconfig.model.Product;
import com.paulo.estudandoconfig.model.ProductSale;
import com.paulo.estudandoconfig.model.Sale;
import com.paulo.estudandoconfig.repository.ProductRepository;
import com.paulo.estudandoconfig.repository.SaleRepository;

public class SaleService extends ContextHolder {

	@Autowired
	private SaleRepository repository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductRepository productRepository;

	public String finishSale(SaleDTO dto) {
		Sale sale = mapper.map(dto, Sale.class);

		sale.getProducts().forEach(productSale -> {
			Product product = productRepository.findById(productSale.getProductId())
					.orElseThrow(() -> new RuntimeException("The product doesnt exist."));
			updateQuantity(productSale, product);
			productSale.setUnitPrice(product.getPrice());

		});

		repository.save(sale.setOwner(getUsername()).calculateTotal().setDate(LocalDateTime.now()));
		return "{}";
	}

	private void updateQuantity(ProductSale p, Product product) {

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
		return repository.findAllByOwner(of, getUsername()).map(this::toDTO);
	}

	public ResponseEntity deleteById(Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);

		}
		return new ResponseEntity<>("Entity not found",HttpStatus.NOT_FOUND);
	}

	public ChartDTO saleChart(Integer year) {
		return isAdmin() ? buildChart(repository.chartSaleMonth(year)) : saleChartByUser(year, getUsername());

	}

	public ChartDTO saleChartByUser(Integer year, String username) {
		return buildChart(repository.chartSaleMonth(year, username));
	}

	public BigDecimal incomeByUsername(Integer month, String username) {
		return repository.totalIncome(month, username);

	}

	private SaleDTO toDTO(Sale sale) {
		return mapper.map(sale, SaleDTO.class);
	}

	private ChartDTO buildChart(List<int[]> arr) {
		return new ChartDTO(arr.stream().map(e -> e[0]).toList(), arr.stream().map(e -> e[1]).toList());
	}
}
