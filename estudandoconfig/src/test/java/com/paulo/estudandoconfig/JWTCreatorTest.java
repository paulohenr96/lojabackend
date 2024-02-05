package com.paulo.estudandoconfig;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.paulo.estudandoconfig.constant.Constants;
import com.paulo.estudandoconfig.dto.TokenDTO;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

class JWTCreatorTest {

	@Test
	void newTokenWithPrefix() {
		
		
	
		
		TokenDTO dto = new TokenDTO().setSubject("admin")
				.setRoles(List.of("admin"));

		
		
		String newToken = JWTCreator.newToken(dto);
		
		assertEquals(Constants.prefix,newToken.split(" ")[0]+" ");
	}
	
	@Test
	void verifyValidToken() {
		
		
		TokenDTO dto = new TokenDTO().setSubject("username")
				.setRoles(List.of("admin"));

		
		
		String validToken = JWTCreator.newToken(dto);
		
		TokenDTO token = JWTCreator.verifyToken(validToken.split(" ")[1]);
		
		assertNotNull(token);
		assertEquals("username",token.getSubject());
		assertTrue(token.getRoles().contains("admin"));
		
		
	}
	
	@Test
	void verifyExpiredToken() {
		
		
		TokenDTO dto = new TokenDTO().setSubject("username")
				.setRoles(List.of("admin"));

		
		
		String validToken = Jwts.builder().setSubject(dto.getSubject())
				.claim("roles",dto.getRoles())
				.setIssuedAt(Date.from(Instant.now()))
				.setExpiration(new Date(System.currentTimeMillis())).compact();
		
		
		assertThrows(ExpiredJwtException.class, ()->JWTCreator.verifyToken(validToken));
		
		
		
	}

}
