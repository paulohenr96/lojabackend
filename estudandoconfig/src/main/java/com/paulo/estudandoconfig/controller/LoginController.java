package com.paulo.estudandoconfig.controller;

import java.util.List;

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

@CrossOrigin(origins ="http://localhost:4200/",allowCredentials = "true",allowedHeaders = "*")
@Controller
public class LoginController {
	

	
	
	@PostMapping("login")
	public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO login) {
		TokenDTO dto = new TokenDTO();
		dto.setRoles(List.of("user","admin"));
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
