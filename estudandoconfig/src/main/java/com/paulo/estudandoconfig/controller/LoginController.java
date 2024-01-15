package com.paulo.estudandoconfig.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.paulo.estudandoconfig.model.UserAccount;
import com.paulo.estudandoconfig.repository.UserAccountRepository;

@CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true", allowedHeaders = "*")
@Controller
public class LoginController {

	@Autowired
	private UserAccountRepository repository;

	@PostMapping("login")
	public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO login) {

		return repository.findByUserName(login.getUsername()).map((e) -> {
			if (!new BCryptPasswordEncoder().matches(login.getPassword(), e.getPassword()))
				return ResponseEntity.ok(new TokenDTO());

			TokenDTO dto = new TokenDTO();
			dto.setSubject(e.getUserName());
			dto.setRoles(e.getRoles().stream().map(e2 -> e2.getName()).toList());
			
			
			String token = JWTCreator.newToken(dto);
			dto.setFullToken(token);
			dto.setGoal(e.getMonthlyGoal());
			return ResponseEntity.ok(dto);
		}).orElse(ResponseEntity.ok(new TokenDTO()));


	}

	@GetMapping("logout")
	public ResponseEntity<String> logout() {
		SecurityContextHolder.clearContext();
		return new ResponseEntity<String>("Ok", HttpStatus.OK);
	}

}
