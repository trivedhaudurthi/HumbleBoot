package com.northeastern.edu.models;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationResponse {
	private String jwt;
	public AuthenticationResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public AuthenticationResponse(String jwt) {
		super();
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

}
