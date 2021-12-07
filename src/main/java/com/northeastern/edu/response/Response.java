package com.northeastern.edu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.stereotype.Component;

@JsonInclude(Include.NON_NULL)
@Component
public class Response implements ResponseAPI{
	private int status;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}	
	public static Response create(String message) {
		return new Response(message);
	}
}
