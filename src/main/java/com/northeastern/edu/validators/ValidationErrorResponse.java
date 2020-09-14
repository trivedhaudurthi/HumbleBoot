package com.northeastern.edu.validators;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {
	private List<Violation> violations= new ArrayList<>();
	public ValidationErrorResponse() {
		// TODO Auto-generated constructor stub
	}
	public List<Violation> getViolations() {
		return violations;
	}
	public void setViolations(List<Violation> violations) {
		this.violations = violations;
	}
	
}
