package com.paulo.estudandoconfig.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.paulo.estudandoconfig.context.ContextHolder;
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
	public ResponseEntity<String> newUser(@RequestBody UserAccountDTO user) {

		return repo.findByUserName(user.getUserName()).map((e) -> ResponseEntity.ok(("Invalid Username")))
				.orElseGet(() -> criptoAndSaveDTO.apply(user));
	}

	@PutMapping
	public ResponseEntity<String> edit(@RequestBody UserAccountDTO userDTO) {
		return repo.findById(userDTO.getId()).map(user -> checkUsername(userDTO, user))
				.orElseGet(() -> ResponseEntity.ok("fail"));

	}

	@PutMapping("newpassword")
	public ResponseEntity<String> newPassword(@RequestBody UserAccountDTO user) {
		return repo.findById(user.getId()).map(e -> {
			e.setPassword(cripto.apply(user.getPassword()));
			repo.save(e);
			return ResponseEntity.ok("");
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping
	public ResponseEntity<List<UserAccountDTO>> getAll() {
		return ResponseEntity.ok(repo.findAll().stream().map(UserAccount::toDTO).toList());
	}

	@GetMapping("{id}")
	public ResponseEntity<UserAccountDTO> getById(@PathVariable(name = "id") Long id) {
		return repo.findById(id)
				.map(UserAccount::toDTO)
				.map(ResponseEntity::ok)
				.get();
	}

	@DeleteMapping("{id}")
	public ResponseEntity deleteById(@PathVariable(name = "id") Long id) {
		if (repo.existsById(id)) {
			repo.deleteById(id);
			return ResponseEntity.ok("");
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@GetMapping("metrics")
	public ResponseEntity<Metrics> getMetrics() {
		return metricsRepository.findAll()
				.stream()
				.findFirst()
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.ok(new Metrics()));
	}

	@PostMapping("confirmpassword")
	public ResponseEntity<String> confirmPassword(@RequestBody String password) {
		String username = ContextHolder.getUsername();

		BiPredicate<String, String> matchPassword = (raw, crypto) -> new BCryptPasswordEncoder().matches(raw, crypto);

		
		return matchPassword.test(password,
								repo.findByUserName(username)
								.get().getPassword())?
				new ResponseEntity<>(HttpStatus.OK):
				new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
	}

	@PutMapping("metrics")
	public ResponseEntity<Metrics> editMetrics(@RequestBody Metrics m) {

		metricsRepository.save(m);
		return ResponseEntity.ok(m);
	}

	private UnaryOperator<String> cripto = raw -> new BCryptPasswordEncoder().encode(raw);
	
	private ResponseEntity<String> checkUsername(UserAccountDTO userDTO, UserAccount user) {

		boolean usernameChanged = !user.getUserName().equalsIgnoreCase(userDTO.getUserName());
		if (usernameChanged) {
			boolean existUserWithSameUsername = repo.findByUserName(userDTO.getUserName()).isPresent();
			if (existUserWithSameUsername)
				return new ResponseEntity(HttpStatus.CONFLICT);
		}
		userDTO.setPassword(user.getPassword());
		return saveDTO.apply(userDTO);
	}

	private Function<UserAccount, ResponseEntity<String>> save = user -> {
		repo.save(user);
		return new ResponseEntity(HttpStatus.OK);

	};
	private Function<List<String>, Set<Role>> rolesNamesToObjects = names -> names.stream()
			.map(name -> roleRepo.findByName(name)).map(Optional::get).collect(Collectors.toSet());

	private Function<UserAccountDTO, UserAccount> toEntity = (user) -> {
		UserAccount account = mapper.map(user, UserAccount.class);
		account.setRoles(rolesNamesToObjects.apply(user.getRolesName()));
		return account;
	};

	private Function<UserAccountDTO, ResponseEntity<String>> saveDTO = (user) -> toEntity.andThen(save).apply(user);;

	private Function<UserAccountDTO, ResponseEntity<String>> criptoAndSaveDTO = (user) -> {
		user.setPassword(cripto.apply(user.getPassword()));
		return saveDTO.apply(user);
	};
}
