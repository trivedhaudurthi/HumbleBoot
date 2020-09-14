package com.northeastern.edu.models;

import org.springframework.stereotype.Component;

@Component
public class Response {
	private String message;
	public Response() {
		// TODO Auto-generated constructor stub
	}
	public Response(String message) {
		this.message=message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static Response create(String message) {
		return new Response(message);
	}
}
