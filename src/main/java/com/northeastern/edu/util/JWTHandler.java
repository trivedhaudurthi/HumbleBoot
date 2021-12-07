package com.northeastern.edu.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTHandler {
	private String SECRET_KEY ="secret";	
	public JWTHandler() {
		
	}
	public String extractSubject(String token) {
		return extract(token,Claims::getSubject);
	}
	public Date extractExpiration(String token) {
		return extract(token,Claims::getExpiration);
	}
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	public <T> T extract(String token, Function<Claims,T> claimsResolver) {
		Claims allClaims= extractAllClaims(token);
		return claimsResolver.apply(allClaims);
	}
	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(this.SECRET_KEY).parseClaimsJws(token).getBody();
	}
	public String generateToken(String payload) {
		Map<String,Object> claims= new HashMap<String, Object>();
		return createToken(claims, payload);
	}
	private String createToken(Map<String,Object> claims,String payload) {
		return Jwts.builder().setClaims(claims).setSubject(payload).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+1000*60*60*90))
				.signWith(SignatureAlgorithm.HS256, this.SECRET_KEY).compact();
	}
	public boolean validateToken(String token, UserDetails userDetails) {
		String username=extractSubject(token);
		return username.equals(userDetails.getUsername())&&!isTokenExpired(token);
	}
}
