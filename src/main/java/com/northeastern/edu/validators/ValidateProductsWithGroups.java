package com.northeastern.edu.validators;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.northeastern.edu.models.Product;

@Service
@Validated
public class ValidateProductsWithGroups {

	public ValidateProductsWithGroups() {
		// TODO Auto-generated constructor stub
	}
	@Validated(OnCreate.class)
	public void validateOnCreate(Product product) {
		
	}
	@Validated(OnUpdate.class)
	public void validateOnUpdate(Product product) {
		
	}

}
