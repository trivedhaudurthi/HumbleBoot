package com.northeastern.edu.validators;

public class Violation{
	private String fieldName;
	private String message;
	public Violation(String fieldname, String message) {
		// TODO Auto-generated constructor stub
		this.fieldName=fieldname;
		this.message= message;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
