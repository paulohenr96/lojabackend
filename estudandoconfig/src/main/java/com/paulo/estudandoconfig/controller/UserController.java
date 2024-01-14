package com.paulo.estudandoconfig.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.paulo.estudandoconfig.util.ControllerUtil;

@CrossOrigin(origins ="*" )

@Controller
@RequestMapping("users")

public class UserController extends ControllerUtil{
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
			cripto(account);
			repo.save(account);
			return ResponseEntity.ok("");

		}
		return new ResponseEntity<String>("",HttpStatus.CONFLICT);

	}
	private void cripto(UserAccount account) {
		String password = account.getPassword();
		account.setPassword(new BCryptPasswordEncoder().encode(password));
	}
	@PutMapping
	public ResponseEntity<String> edit(@RequestBody UserAccountDTO user) {
		
		Optional<UserAccount> optional = repo.findById(user.getId());
		if (!optional.isEmpty()) {
			
			UserAccount ac = optional.get();
			if (!ac.getUserName().equals(user.getUserName())) {
				if (repo.findByUserName(user.getUserName()).isPresent())return ResponseEntity.ok(super.responseJson("Invalid username"));
			}
			
			UserAccount account = mapper.map(user, UserAccount.class);
			
			account.setRoles(new HashSet<>(user.getRolesName().stream().map(e->roleRepo.findByName(e).get()).toList()));
			account.setPassword(ac.getPassword());
			repo.save(account);
			
			
			
			
			
			return ResponseEntity.ok("");

		}
		return ResponseEntity.ok("fail");

	}
	
	
	@PutMapping("newpassword")
	public ResponseEntity<String> newPassword(@RequestBody UserAccountDTO user) {
		
		Optional<UserAccount> optional = repo.findById(user.getId());
		if (!optional.isEmpty()) {
			
			UserAccount ac = optional.get();
			
			
			ac.setPassword(user.getPassword());
			cripto(ac);

			repo.save(ac);
			
			
			
			
			
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
	@PreAuthorize("hasAuthority('admin')")
	@PostMapping("confirmpassword")
	public ResponseEntity<String> confirmPassword(@RequestBody String password) {
		String username=SecurityContextHolder.getContext().getAuthentication().getName();
		
		 boolean matches = new BCryptPasswordEncoder()
			.matches(password,repo.findByUserName(username)
					.get()
					.getPassword());
			return ResponseEntity.ok(super.responseJson(matches+""));

	}
	
	
	@PutMapping("metrics")
	public ResponseEntity<Metrics> editMetrics(@RequestBody Metrics m) {
		
		metricsRepository.save(m);
		return ResponseEntity.ok(m);
	}
}
