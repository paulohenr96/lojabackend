package com.paulo.estudandoconfig.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paulo.estudandoconfig.dto.InfoDTO;
import com.paulo.estudandoconfig.dto.ProductDTO;
import com.paulo.estudandoconfig.service.ProductService;
@CrossOrigin(origins ="http://localhost:4200/")
@Controller
@RequestMapping("products")
public class ProductController {
	
	@Autowired
	private  ProductService service;
	
	@PostMapping
	public ResponseEntity<String> saveProduct(@RequestBody ProductDTO dto) {
		
		String save = service.save(dto);
		System.out.println("Teste");
		return ResponseEntity.ok("");
	}

	@GetMapping
	public ResponseEntity<Page<ProductDTO>> getAll(@RequestParam(name="page",defaultValue = "0") Integer page,
			@RequestParam(name="size",defaultValue = "3") Integer size) {
		return ResponseEntity.ok(service.getAll(PageRequest.of(page,size)));
	}

	@GetMapping("category/{category}")
	public ResponseEntity<Page<ProductDTO>> getAllByCategory(@RequestParam(name="page",defaultValue = "0") Integer page,
			@RequestParam(name="size",defaultValue = "3") Integer size,
			@PathVariable(name="category") String category) {
		return ResponseEntity.ok(service.getAll(PageRequest.of(page,size),category));
	}
	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO p,@PathVariable("id") Long id ){
		return ResponseEntity.ok(service.updateById(p,id));
	}
	
	
	
	@PreAuthorize("hasAuthority('admin')")
	@DeleteMapping("/{id}")
	public ResponseEntity<ProductDTO> deleteProduct(@PathVariable("id") Long id ){
		
		return ResponseEntity.ok(service.deleteById(id));
	}
	

	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable("id") Long id ){
		return ResponseEntity.ok(service.findById(id));

	}

	@GetMapping("infos")
	public ResponseEntity<InfoDTO> countProduct(){
		return ResponseEntity.ok(service.info());

	}
	
	


	@GetMapping("checkquantity")
	public ResponseEntity<List<ProductDTO>> checkQuantity(){
		return ResponseEntity.ok(service.checkQuantity());

	}




}
