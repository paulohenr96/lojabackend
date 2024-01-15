package com.paulo.estudandoconfig.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

@CrossOrigin(origins = "*")

@Controller
@RequestMapping("users")

public class UserController extends ControllerUtil {
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
			cripto(user);
			return saveDTO(user);

		}

		return new ResponseEntity<String>("", HttpStatus.CONFLICT);

	}

	@PutMapping
	public ResponseEntity<String> edit(@RequestBody UserAccountDTO user) {
		return repo.findById(user.getId()).map(e -> {
			if ((!e.getUserName().equals(user.getUserName())) && repo.findByUserName(user.getUserName()).isPresent())
				return ResponseEntity.ok(super.responseJson("Invalid username"));
			user.setPassword(e.getPassword());
			return saveDTO(user);
		}).orElse(ResponseEntity.ok("fail"));
	}

	@PutMapping("newpassword")
	public ResponseEntity<String> newPassword(@RequestBody UserAccountDTO user) {

		return repo.findById(user.getId()).map(e -> {
			cripto(user);
			e.setPassword(user.getPassword());
			repo.save(e);
			return ResponseEntity.ok("");
		}).orElse(ResponseEntity.ok("fail"));

	}

//	@Secured(value="dfgdfgdfgdf")
	@PreAuthorize("hasAuthority('admin')")

	@GetMapping
	public ResponseEntity<List<UserAccountDTO>> getAll() {
		return ResponseEntity.ok(repo.findAll().stream().map(this::toDTO).toList());
	}

	@GetMapping("{id}")
	public ResponseEntity<UserAccountDTO> getById(@PathVariable(name = "id") Long id) {
		UserAccount account = repo.findById(id).get();

		return ResponseEntity.ok(toDTO(account));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteById(@PathVariable(name = "id") Long id) {

		if (repo.existsById(id)) {
			repo.deleteById(id);
			return ResponseEntity.ok("");
		}

		return ResponseEntity.ok("{User Not Found}");

	}

	@GetMapping("metrics")
	public ResponseEntity<Metrics> getMetrics() {
		List<Metrics> list = metricsRepository.findAll();
		Metrics m = new Metrics();
		if (list.size() > 0) {
			m = list.get(0);
		}
		return ResponseEntity.ok(m);
	}

	@PreAuthorize("hasAuthority('admin')")
	@PostMapping("confirmpassword")
	public ResponseEntity<String> confirmPassword(@RequestBody String password) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean matches = new BCryptPasswordEncoder().matches(password,
				repo.findByUserName(username).get().getPassword());
		return ResponseEntity.ok(super.responseJson(matches + ""));
	}

	@PutMapping("metrics")
	public ResponseEntity<Metrics> editMetrics(@RequestBody Metrics m) {

		metricsRepository.save(m);
		return ResponseEntity.ok(m);
	}

	private void cripto(UserAccountDTO account) {
		String password = account.getPassword();
		account.setPassword(new BCryptPasswordEncoder().encode(password));
	}

	private UserAccountDTO toDTO(UserAccount u) {
		UserAccountDTO dto = mapper.map(u, UserAccountDTO.class);
		dto.setRolesName(u.getRoles().stream().map(r -> r.getName()).toList());
		return dto;
	}

	private ResponseEntity<String> saveDTO(UserAccountDTO user) {
		UserAccount account = mapper.map(user, UserAccount.class);

		account.setRoles(new HashSet<>(user.getRolesName().stream().map(e -> roleRepo.findByName(e).get()).toList()));
		repo.save(account);
		return ResponseEntity.ok("");
	}

}
