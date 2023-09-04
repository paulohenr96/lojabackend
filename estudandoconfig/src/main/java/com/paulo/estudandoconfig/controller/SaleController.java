package com.paulo.estudandoconfig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.paulo.estudandoconfig.dto.SaleDTO;
import com.paulo.estudandoconfig.service.SaleService;

@Controller
@RequestMapping("sale")
public class SaleController {

	
		@Autowired
		private SaleService service;
	
		
		
		
		@PostMapping
		public ResponseEntity<String> save(@RequestBody SaleDTO dto){
			service.finishSale(dto);
			
			
			return ResponseEntity.ok("salvo");
		}
		
}
