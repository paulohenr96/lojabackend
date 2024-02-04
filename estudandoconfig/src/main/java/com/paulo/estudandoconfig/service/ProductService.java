package com.paulo.estudandoconfig.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.paulo.estudandoconfig.context.ContextHolder;
import com.paulo.estudandoconfig.dto.InfoDTO;
import com.paulo.estudandoconfig.dto.ProductDTO;
import com.paulo.estudandoconfig.model.Product;
import com.paulo.estudandoconfig.repository.ProductRepository;
import com.paulo.estudandoconfig.repository.SaleRepository;

@Service
public class ProductService extends ContextHolder {

	public ProductService(ProductRepository repository, SaleRepository saleRepository, ModelMapper modelMapper) {
		this.repository = repository;
		this.saleRepository = saleRepository;
		this.modelMapper = modelMapper;
	}

	private final ProductRepository repository;
	private final SaleRepository saleRepository;

	private final ModelMapper modelMapper;

	public String save(ProductDTO produtoDTO) {

		Product map = modelMapper.map(produtoDTO, Product.class);
		map.setId(null);
		repository.save(map);
		return "";
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

	public ResponseEntity deleteById(Long id) {
		// TODO Auto-generated method stub

		return repository.findById(id).map((e) -> {
			repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

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

		product
		.setName(p.getName())
		.setQuantity(p.getQuantity())
		.setPrice(p.getPrice())
		.setCategory(p.getCategory());

		Product save = repository.save(product);
		return toDTO(save);
	}

	private BigDecimal income(Integer month) {
		// TODO Auto-generated method stub

		if (isAdmin())

		{
			return saleRepository.totalIncome(month);
		}
		String username = getUsername();
		return saleRepository.totalIncome(month, username);
	}

	private ProductDTO toDTO(Product p) {
		return modelMapper.map(p, ProductDTO.class);
	}
}
