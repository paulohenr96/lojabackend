package com.paulo.estudandoconfig.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paulo.estudandoconfig.dto.UserAccountDTO;
import com.paulo.estudandoconfig.model.Metrics;
import com.paulo.estudandoconfig.model.Role;
import com.paulo.estudandoconfig.model.UserAccount;
import com.paulo.estudandoconfig.repository.MetricRepository;
import com.paulo.estudandoconfig.repository.RoleRepository;
import com.paulo.estudandoconfig.repository.UserAccountRepository;
import com.paulo.estudandoconfig.util.ControllerUtil;

@CrossOrigin(origins = "*")

@RestController
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

		return repo.findByUserName(user.getUserName()).map((e) -> ResponseEntity.ok(responseJson("Invalid Username")))
				.orElseGet(() -> criptoAndSaveDTO(user));
	}

	@PutMapping
	public ResponseEntity<String> edit(@RequestBody UserAccountDTO userDTO) {
		return repo.findById(userDTO.getId()).map(user -> extracted(userDTO, user))
				.orElseGet(() -> ResponseEntity.ok("fail"));

	}

	@PutMapping("newpassword")
	public ResponseEntity<String> newPassword(@RequestBody UserAccountDTO user) {
		return repo.findById(user.getId()).map(e -> {
			e.setPassword(cripto.apply(user.getPassword()));
			repo.save(e);
			return ResponseEntity.ok("");
		}).orElseGet(() -> ResponseEntity.ok("fail"));
	}

	@GetMapping
	public ResponseEntity<List<UserAccountDTO>> getAll() {
		return ResponseEntity.ok(repo.findAll().stream().map(toDTO::apply).toList());
	}

	@GetMapping("{id}")
	public ResponseEntity<UserAccountDTO> getById(@PathVariable(name = "id") Long id) {

		return repo.findById(id).map(toDTO::apply).map(ResponseEntity::ok).get();
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
		return metricsRepository.findAll().stream().findFirst().map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.ok(new Metrics()));
	}

	@PostMapping("confirmpassword")
	public ResponseEntity<String> confirmPassword(@RequestBody String password) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		BiPredicate<String, String> matchPassword = (raw, crypto) -> new BCryptPasswordEncoder().matches(raw, crypto);
		
		
		String responseString = String.valueOf(matchPassword.test(password, repo.findByUserName(username).get().getPassword()));
		String responseJson = super.responseJson(responseString);
		return ResponseEntity.ok(responseJson);
	}

	@PutMapping("metrics")
	public ResponseEntity<Metrics> editMetrics(@RequestBody Metrics m) {

		metricsRepository.save(m);
		return ResponseEntity.ok(m);
	}

	private Function<String, String> cripto = raw -> new BCryptPasswordEncoder().encode(raw);

	private Function<UserAccount, UserAccountDTO> toDTO = (u) -> {
		UserAccountDTO dto = mapper.map(u, UserAccountDTO.class);
		dto.setRolesName(u.getRoles().stream().map(Role::getName).toList());

		return dto;
	};

	private ResponseEntity<String> extracted(UserAccountDTO userDTO, UserAccount user) {

		boolean usernameChanged = !user.getUserName().equalsIgnoreCase(userDTO.getUserName());
		if (usernameChanged) {
			boolean existUserWithSameUsername = repo.findByUserName(userDTO.getUserName()).isPresent();
			if (existUserWithSameUsername)
				return ResponseEntity.ok(super.responseJson("Invalid username"));
		}
		return editAndSaveDTO(userDTO, user.getPassword());
	}

	private ResponseEntity<String> editAndSaveDTO(UserAccountDTO userDTO, String p) {
		userDTO.setPassword(p);
		return saveDTO(userDTO);
	}

	private Function<List<String>, Set<Role>> rolesNamesToObjects = names -> names.stream()
			.map(name -> roleRepo.findByName(name)).map(Optional::get).collect(Collectors.toSet());

	private ResponseEntity<String> saveDTO(UserAccountDTO user) {
		UserAccount account = mapper.map(user, UserAccount.class);
		account.setRoles(rolesNamesToObjects.apply(user.getRolesName()));
		repo.save(account);
		return ResponseEntity.ok("");
	}

	private ResponseEntity<String> criptoAndSaveDTO(UserAccountDTO user) {
		user.setPassword(cripto.apply(user.getPassword()));
		return saveDTO(user);
	}
}
