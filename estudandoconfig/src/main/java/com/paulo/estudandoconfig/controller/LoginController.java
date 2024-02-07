package com.paulo.estudandoconfig.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.paulo.estudandoconfig.JWTCreator;
import com.paulo.estudandoconfig.dto.LoginDTO;
import com.paulo.estudandoconfig.dto.TokenDTO;
import com.paulo.estudandoconfig.repository.UserAccountRepository;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")

@Controller
public class LoginController {

	
	public LoginController(UserAccountRepository repository) {
		super();
		this.repository = repository;
	}

	private final UserAccountRepository repository;

	@PostMapping("login")
	public ResponseEntity login(@Valid @RequestBody LoginDTO login) {

		return repository.findByUserName(login.getUsername()).map((e) -> {
			if (!new BCryptPasswordEncoder().matches(login.getPassword(), e.getPassword()))
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			TokenDTO dto = new TokenDTO().setSubject(e.getUserName())
					.setRoles(e.getRoles().stream().map(e2 -> e2.getName()).toList());

			String token = JWTCreator.newToken(dto);
			dto.setFullToken(token).setGoal(e.getMonthlyGoal());
			return ResponseEntity.ok(dto);
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));

	}

	@GetMapping("logout")
	public ResponseEntity<String> logout() {
		SecurityContextHolder.clearContext();
		return new ResponseEntity<String>("Ok", HttpStatus.OK);
	}

}
