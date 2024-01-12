package com.paulo.estudandoconfig.controller;

import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.paulo.estudandoconfig.dto.UserAccountDTO;
import com.paulo.estudandoconfig.model.Metrics;
import com.paulo.estudandoconfig.model.UserAccount;
import com.paulo.estudandoconfig.repository.MetricRepository;
import com.paulo.estudandoconfig.repository.RoleRepository;
import com.paulo.estudandoconfig.repository.UserAccountRepository;

@CrossOrigin(origins ="*" )

@Controller
@RequestMapping("users")

public class UserController {
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UserAccountRepository repo;

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private MetricRepository metricsRepository;
	
	@PostMapping
	public ResponseEntity<String> save(@RequestBody UserAccountDTO user) {
		user.setId(null);
		
		
		
		if (repo.findByUserName(user.getUserName()).isEmpty()) {

			
			UserAccount account = mapper.map(user, UserAccount.class);
			account.setRoles(new HashSet<>(user.getRolesName().stream().map(e->roleRepo.findByName(e).get()).toList()));
			
			repo.save(account);
			return ResponseEntity.ok("ok");

		}
		return ResponseEntity.ok("fail");

	}
	@PutMapping
	public ResponseEntity<String> edit(@RequestBody UserAccountDTO user) {
		if (!repo.findByUserName(user.getUserName()).isEmpty()) {

			
			
			
			UserAccount account = mapper.map(user, UserAccount.class);
			account.setRoles(new HashSet<>(user.getRolesName().stream().map(e->roleRepo.findByName(e).get()).toList()));
			
			repo.save(account);
			
			
			
			
			
			return ResponseEntity.ok("");

		}
		return ResponseEntity.ok("fail");

	}
//	@Secured(value="dfgdfgdfgdf")
	@PreAuthorize("hasAuthority('admin')")

	@GetMapping
	public ResponseEntity<List<UserAccountDTO>> getAll() {
		return ResponseEntity.ok(

				repo.findAll().stream().map(e -> {
					UserAccountDTO dto = mapper.map(e, UserAccountDTO.class);

					dto.setRolesName(e.getRoles().stream().map(r -> r.getName()).toList());
					return dto;

				}).toList());
	}

	@GetMapping("{id}")
	public ResponseEntity<UserAccountDTO> getById(@PathVariable(name = "id") Long id) {
		UserAccount account = repo.findById(id).get();
		UserAccountDTO dto = mapper.map(account, UserAccountDTO.class);
		dto.setRolesName(account.getRoles().stream().map(e->e.getName()).toList());
		return ResponseEntity.ok(dto);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteById(@PathVariable(name="id")Long id){
		
		if (repo.existsById(id)) {
			repo.deleteById(id);
			return ResponseEntity.ok("");
		};
		return ResponseEntity.ok("{User Not Found}");
		
		
		
	}
	
	@GetMapping("metrics")
	public ResponseEntity<Metrics> getMetrics() {
		List<Metrics> list = metricsRepository.findAll();
		Metrics m=new Metrics();
		if (list.size()>0) {
			m=list.get(0);
		}
		return ResponseEntity.ok(m);
	}
	@PutMapping("metrics")
	public ResponseEntity<Metrics> editMetrics(@RequestBody Metrics m) {
		
		metricsRepository.save(m);
		return ResponseEntity.ok(m);
	}
}
