package com.northeastern.edu.controllers;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.northeastern.edu.CustomExceptions.DuplicateProductException;
import com.northeastern.edu.CustomExceptions.DuplicateUserException;
import com.northeastern.edu.CustomExceptions.PaymentTypeNotFoundException;
import com.northeastern.edu.response.Response;
import com.northeastern.edu.validators.ValidationErrorResponse;
import com.northeastern.edu.validators.Violation;;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {
	
	@ExceptionHandler({TransactionSystemException.class})
	@ResponseBody
	ResponseEntity<Object> onConstraintViolationException(Exception e) {
		Throwable cause= ((TransactionSystemException)e).getRootCause();
		System.out.println((cause.getMessage()));
		if(cause instanceof ConstraintViolationException) {
			ConstraintViolationException conexp= (ConstraintViolationException) cause;
			ValidationErrorResponse response= new ValidationErrorResponse();
			for(ConstraintViolation<?> violation: conexp.getConstraintViolations()) {
				response.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
			}
			return new ResponseEntity<Object>(response,HttpStatus.BAD_REQUEST);
		}
		System.out.println(cause.toString());
		return new ResponseEntity<Object>(new Response("Something went wrong"),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	ResponseEntity<Response> onSQLIntegrityConstraintViolationException(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().body(Response.create(e.getMessage()));
	}
	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseBody
	ResponseEntity<Response> onEmptyResultDataAccessException(EmptyResultDataAccessException e){
		return ResponseEntity.badRequest().body(Response.create("Product does not exist"));
	}

	@ExceptionHandler(DuplicateProductException.class)
	@ResponseBody
	ResponseEntity<Response> onDuplicateProductsFound(DuplicateProductException e){
		return ResponseEntity.badRequest().body(Response.create(e.getMessage()));
	}

	@ExceptionHandler(DuplicateUserException.class)
	@ResponseBody
	ResponseEntity<Response> onDuplicateUsersFound(DuplicateUserException e){
		return ResponseEntity.badRequest().body(Response.create(e.getMessage()));
	}

	@ExceptionHandler(PaymentTypeNotFoundException.class)
	@ResponseBody
	ResponseEntity<Response> onPaymentTypeNotFound(PaymentTypeNotFoundException e){
		return ResponseEntity.badRequest().body(Response.create(e.getMessage()));
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseBody
	ResponseEntity<Response> onUserNameNotFound(UsernameNotFoundException e){
		return ResponseEntity.status(403).body(Response.create(e.getMessage()));
	}

	// io.jsonwebtoken.MalformedJwtException
}
