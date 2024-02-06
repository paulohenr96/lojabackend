package com.paulo.estudandoconfig.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paulo.estudandoconfig.dto.InfoDTO;
import com.paulo.estudandoconfig.dto.ProductDTO;
import com.paulo.estudandoconfig.service.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("products")
public class ProductController {

	public ProductController(ProductService service) {
		super();
		this.service = service;
	}

	private ProductService service;

	@PostMapping
	public ResponseEntity<String> saveProduct(@Valid @RequestBody ProductDTO dto) {

		service.save(dto);
		return ResponseEntity.ok("");
	}

	@GetMapping
	@Valid
	public ResponseEntity<Page<ProductDTO>> getAll
	( @Min(value=0,message ="{page.notnegative}") @RequestParam(name = "page", defaultValue = "0") 
	 Integer page,
			@RequestParam(name = "size", defaultValue = "3") Integer size) {
		return ResponseEntity.ok(service.getAll(PageRequest.of(page, size)));
	}

	@GetMapping("category/{category}")
	public ResponseEntity<Page<ProductDTO>> getAllByCategory(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "3") Integer size,
			@PathVariable(name = "category")@NotBlank String category ) {
		return ResponseEntity.ok(service.getAll(PageRequest.of(page, size), category));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id,@RequestBody ProductDTO p) {
		return ResponseEntity.ok(service.updateById(p, id));
	}

	@PreAuthorize("hasAuthority('admin')")
	@DeleteMapping("/{id}")
	public ResponseEntity deleteProduct(@PathVariable("id") Long id) {

		return service.deleteById(id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.findById(id));

	}

	@GetMapping("infos")
	public ResponseEntity<InfoDTO> countProduct() {
		return ResponseEntity.ok(service.info());

	}

	@GetMapping("checkquantity")
	public ResponseEntity<List<ProductDTO>> checkQuantity() {
		return ResponseEntity.ok(service.checkQuantity());

	}

}
