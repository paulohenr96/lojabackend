package com.paulo.estudandoconfig.service;

import java.util.Optional;
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
		if (pageRequest.getPageNumber()<0) {
			pageRequest=PageRequest.of(0,pageRequest.getPageSize());
		}
		return repository.findAll(pageRequest)
				.map(e -> modelMapper.map(e, ProductDTO.class));
	}

	public ProductDTO updateById(ProductDTO p, Long id) {
		// TODO Auto-generated method stub
		
		Optional<Product> productOpt = repository.findById(id);
		
		if (productOpt.isPresent()) {
			
			Product product = productOpt.get();
			
			product.setName(p.getName());
			product.setQuantity(p.getQuantity());
			product.setPrice(p.getPrice());
			
			Product save = repository.save(product);
			return modelMapper.map(save, ProductDTO.class);
			
		}
		
		return null;
	}

	public  ProductDTO deleteById(Long id) {
		// TODO Auto-generated method stub
		
		
		Optional<Product> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new RuntimeException("Product does not exist.");
		}
		
		repository.deleteById(id);
		
		return modelMapper.map(optional.get(), ProductDTO.class);
	}

	public ProductDTO findById(Long id) {
		// TODO Auto-generated method stub
		return modelMapper.map(repository.findById(id).get(), ProductDTO.class);
	}
	

	
}
