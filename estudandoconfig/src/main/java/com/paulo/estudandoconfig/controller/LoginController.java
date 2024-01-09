package com.paulo.estudandoconfig.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

@CrossOrigin(origins ="http://localhost:4200/",allowCredentials = "true",allowedHeaders = "*")
@Controller
public class LoginController {
	
	@Autowired
	private  UserAccountRepository repository;
	
	
	@PostMapping("login")
	public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO login) {
		
		Optional<UserAccount> userOpt = repository.findByUserName(login.getUsername());

		if (userOpt.isEmpty()) {
			return ResponseEntity.ok(new TokenDTO());
		}
		UserAccount userAccount = userOpt.get();
		if (!userAccount.getPassword().equals(login.getPassword()))return ResponseEntity.ok(new TokenDTO());
			
		
		
		
		TokenDTO dto = new TokenDTO();
		dto.setSubject(userAccount.getUserName());
		dto.setRoles(userAccount.getRoles().stream().map(e->e.getName()).toList());
		String token = JWTCreator.newToken(dto);
		System.out.println(token);
		dto.setFullToken(token);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("logout")
	public ResponseEntity<String> logout() {
		SecurityContextHolder.clearContext();
		return new ResponseEntity<String>("Ok", HttpStatus.OK);
	}

}
