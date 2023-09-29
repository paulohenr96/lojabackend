package com.paulo.estudandoconfig;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.paulo.estudandoconfig.constant.Constants;
import com.paulo.estudandoconfig.dto.TokenDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTCreator {
	
	
	
	public static String newToken(TokenDTO t) {
		String token="";
		
		JwtBuilder builder = Jwts.builder();
		JwtBuilder claim = builder.setSubject(t.getSubject()).claim("roles", t.getRoles());
		
		String compact = setDates(claim)
				.signWith(SignatureAlgorithm.HS256,Constants.SECRET_KEY)
				.compact();
		return Constants.prefix.concat(compact);
			
		
	}
	
	
	
	public static TokenDTO verifyToken(String token) {
		TokenDTO t=new TokenDTO();
		
		
		Claims body = Jwts.parserBuilder()
				.setSigningKey(Constants.SECRET_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody();
		boolean before = body.getIssuedAt().before(new Date());
		if (before) {
			t.setRoles((List<String>)body.get("roles"));
			t.setSubject(body.getSubject());
			return t;
		}
		return null;
	}
	
	
	
	
	
	private static JwtBuilder setDates(JwtBuilder claim) {
		Date now= Date.from(Instant.now());
		JwtBuilder setIssuedAt = claim.setIssuedAt(now);
		return setIssuedAt.setExpiration(new Date(System.currentTimeMillis()+Constants.duration));
	}
	

}
