package com.paulo.estudandoconfig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paulo.estudandoconfig.dto.ChartDTO;
import com.paulo.estudandoconfig.dto.SaleDTO;
import com.paulo.estudandoconfig.service.SaleService;
@CrossOrigin(origins ="*" )
@Controller
@RequestMapping("sales")
public class SaleController {

	@Autowired
	private SaleService service;

	@PostMapping
	public ResponseEntity<String> save(@RequestBody SaleDTO dto) {

		return ResponseEntity.ok(service.finishSale(dto));
	}
	
	@GetMapping
	public ResponseEntity<Page<SaleDTO>> findAll(@RequestParam(name="page",defaultValue="0")Integer page,
			@RequestParam(name="size",defaultValue="3")Integer size
			
			) {
		return ResponseEntity.ok(service.findAll(PageRequest.of(page, size)));

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteById(@PathVariable(name = "id") Long id) {

		return ResponseEntity.ok(service.deleteById(id));
	}
	@GetMapping("chart")
	public ResponseEntity<ChartDTO> chart() {
		
		return ResponseEntity.ok(service.saleChart());

	}
	
//	@GetMapping("/income/{month}")
//	public ResponseEntity<BigDecimal> getIncome(@PathVariable(name="month") Integer month) {
//		return ResponseEntity.ok(service.income(month));
//
//	}
}
