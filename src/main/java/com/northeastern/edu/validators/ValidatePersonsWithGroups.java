package com.northeastern.edu.validators;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.northeastern.edu.models.Person;

@Service
@Validated
public class ValidatePersonsWithGroups {

	public ValidatePersonsWithGroups() {
		// TODO Auto-generated constructor stub
	}
	@Validated(OnCreate.class)
	public void validateForCreate(Person person) {
		
	}
	@Validated(OnUpdate.class)
	public void validateForUpdate(Person person) {
		
	}
}
