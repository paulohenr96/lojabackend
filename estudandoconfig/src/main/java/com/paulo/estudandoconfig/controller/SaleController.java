package com.paulo.estudandoconfig.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@CrossOrigin(origins = "*")
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
	public ResponseEntity<Page<SaleDTO>> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "3") Integer size

	) {
		return ResponseEntity.ok(service.findAll(PageRequest.of(page, size)));

	}

	@GetMapping("username")
	public ResponseEntity<Page<SaleDTO>> findAllByUsername(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "3") Integer size

	) {
		return ResponseEntity.ok(service.findAllByUsername(PageRequest.of(page, size)));

	}
	
	
	@PreAuthorize("hasAuthority('admin')")

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteById(@PathVariable(name = "id") Long id) {

		return ResponseEntity.ok(service.deleteById(id));
	}

	@GetMapping("chart")
	public ResponseEntity<ChartDTO> chart(@RequestParam(name = "year", defaultValue = "2024") Integer year		) {
		return ResponseEntity.ok(service.saleChart(year));

	}
	@PreAuthorize("hasAuthority('admin')")
	@GetMapping("chart/username")
	public ResponseEntity<ChartDTO> chartByUsername(@RequestParam(name = "year", defaultValue = "2024") Integer year,
			@RequestParam(name = "username",required = true) String username) {
		return ResponseEntity.ok(service.saleChartByUser(year,username));

	}
	@PreAuthorize("hasAuthority('admin')")
	@GetMapping("income/user")
	public ResponseEntity<BigDecimal> monthlyIncomeByUsername(@RequestParam(name = "year", defaultValue = "2024") Integer year,
			@RequestParam(name = "username",required = true) String username,
			@RequestParam(name = "month",required = true) Integer month) {
		BigDecimal incomeByUsername = service.incomeByUsername(month,username);
		return ResponseEntity.ok(incomeByUsername);

	}
}
