package com.paulo.estudandoconfig.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import com.paulo.estudandoconfig.context.ContextHolder;
import com.paulo.estudandoconfig.dto.InfoDTO;
import com.paulo.estudandoconfig.dto.ProductDTO;
import com.paulo.estudandoconfig.model.Product;
import com.paulo.estudandoconfig.repository.ProductRepository;
import com.paulo.estudandoconfig.repository.SaleRepository;

public class ProductService {

	@Autowired
	private ProductRepository repository;
	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private ModelMapper modelMapper;

	public String save(ProductDTO produtoDTO) {

		Product map = modelMapper.map(produtoDTO, Product.class);
		map.setId(null);
		repository.save(map);
		return "ok";
	}

	public Page<ProductDTO> getAll(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		if (pageRequest.getPageNumber() < 0) {
			pageRequest = PageRequest.of(0, pageRequest.getPageSize());
		}
		return repository.findAll(pageRequest).map(this::toDTO);
	}

	public Page<ProductDTO> getAll(PageRequest pageRequest, String category) {
		// TODO Auto-generated method stub
		if (pageRequest.getPageNumber() < 0) {
			pageRequest = PageRequest.of(0, pageRequest.getPageSize());
		}
		return repository.findAllByCategory(pageRequest, category).map(this::toDTO);
	}

	public ProductDTO updateById(ProductDTO p, Long id) {

		return repository.findById(id).map((e) -> update(e, p)).orElse(null);

	}

	public ProductDTO deleteById(Long id) {
		// TODO Auto-generated method stub

		return repository.findById(id).map((e) -> {
			repository.deleteById(id);
			return toDTO(e);
		}).orElseThrow(() -> new RuntimeException("Product does not exist."));

	}

	public InfoDTO info() {

		InfoDTO info = new InfoDTO(income(LocalDate.now().getMonth().getValue()), count(), sumQuantity());
		return info;
	}

	public List<ProductDTO> checkQuantity() {
		return repository.findAll().stream().filter(e -> e.getQuantity() < 10).map(this::toDTO).toList();
	}

	public ProductDTO findById(Long id) {
		// TODO Auto-generated method stub
		return modelMapper.map(repository.findById(id).get(), ProductDTO.class);
	}

	private Integer sumQuantity() {
		return repository.sumQuantity();
	}

	private Long count() {
		// TODO Auto-generated method stub
		return repository.count();
	}

	private ProductDTO update(Product product, ProductDTO p) {

		product.setName(p.getName());
		product.setQuantity(p.getQuantity());
		product.setPrice(p.getPrice());
		product.setCategory(p.getCategory());

		Product save = repository.save(product);
		return toDTO(save);
	}

	private BigDecimal income(Integer month) {
		// TODO Auto-generated method stub

		if (ContextHolder.isAdmin())

		{
			return saleRepository.totalIncome(month);
		}
		String username = ContextHolder.getUsername();
		return saleRepository.totalIncome(month, username);
	}

	private ProductDTO toDTO(Product p) {
		return modelMapper.map(p, ProductDTO.class);
	}
}
