package com.paulo.estudandoconfig.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.paulo.estudandoconfig.dto.SaleDTO;
import com.paulo.estudandoconfig.model.Sale;
import com.paulo.estudandoconfig.repository.SaleRepository;

public class SaleService {

	
	@Autowired
	private SaleRepository repository;
	
	@Autowired
	private ModelMapper mapper;
	
	
	
	public String finishSale(SaleDTO dto){
		
		
		
		
		Sale map = mapper.map(dto,Sale.class);
		
		
		repository.save(map);
		return "ok";
	}
	
	
}
