package com.paulo.estudandoconfig.service;

import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.paulo.estudandoconfig.dto.ProductDTO;
import com.paulo.estudandoconfig.model.Product;
import com.paulo.estudandoconfig.repository.ProductRepository;

public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public String save(ProductDTO produtoDTO) {
		
		Product map = modelMapper.map(produtoDTO,Product.class);
		System.out.println(map.getId());
		map.setId(null);
		repository.save(map);
		return "ok";
	}

	public Page<ProductDTO> getAll(PageRequest pageRequest) {
		// TODO Auto-generated method stub
		
		return repository.findAll(pageRequest)
				.map(e -> modelMapper.map(e, ProductDTO.class));
	}
	
}
