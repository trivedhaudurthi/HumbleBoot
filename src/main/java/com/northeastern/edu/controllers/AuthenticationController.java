package com.northeastern.edu.controllers;

import java.util.HashSet;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.northeastern.edu.PersonRepository;
import com.northeastern.edu.SellerRepository;
import com.northeastern.edu.UserRepository;
import com.northeastern.edu.CustomExceptions.DuplicateUserException;
import com.northeastern.edu.CustomExceptions.DuplicateUserExceptionFactory;
import com.northeastern.edu.Facade.DBUserFacade;
import com.northeastern.edu.models.AuthenticationResponse;
import com.northeastern.edu.models.LoginForm;
import com.northeastern.edu.models.Seller;
import com.northeastern.edu.models.User;
import com.northeastern.edu.response.Response;
import com.northeastern.edu.response.ResponseAPI;
import com.northeastern.edu.response.ResponseBuilder;
import com.northeastern.edu.util.JWTHandler;
import com.northeastern.edu.validators.ValidatePersonsWithGroups;

@RestController
public class AuthenticationController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	SellerRepository sellerRepo;
	@Autowired
	AuthenticationProvider authProvider;
	@Autowired
	JWTHandler jwtHandler;
	@Autowired
	ValidatePersonsWithGroups validatePersons;

	@Autowired
	PersonRepository personRepository;

	@Autowired
	private DBUserFacade userFacade;

	public AuthenticationController() {
		// TODO Auto-generated constructor stub
	}
	@Transactional
	@PostMapping("/signup/user")
	public ResponseEntity<ResponseAPI> signUp(@RequestBody User user) {
		validatePersons.validateForCreate(user);
		if(userFacade.findUserByEmail(user.getEmail())!=null) {
			throw DuplicateUserExceptionFactory.getInstance().getObject("User with this email exists");
		}
		// User savedUser= userRepo.save(user);
		userFacade.saveUser(user);
		ResponseBuilder rBuilder = new ResponseBuilder();
		rBuilder.addMessage("success");
		rBuilder.addStatus(200);
		return ResponseEntity.ok(rBuilder.getResponse());
	}
	@Transactional
	@PostMapping("/signup/seller")
	public ResponseEntity<Response> signup(@RequestBody Seller seller){
			validatePersons.validateForCreate(seller);
			if(userFacade.findSellerByEmail(seller.getEmail())!=null) {
				throw DuplicateUserExceptionFactory.getInstance().getObject("User with this email exists");
			}
			Seller savedSeller= userFacade.saveSeller(seller);
//			System.out.println(savedSeller.getProducts());
			return ResponseEntity.ok(Response.create("success"));		
	}
	@Transactional
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody LoginForm loginform) throws BadCredentialsException{
	
		try {
			Authentication auth=authProvider.authenticate(new UsernamePasswordAuthenticationToken(loginform.getEmail(), loginform.getPassword()));
			
		}
		catch (BadCredentialsException e) {
			
			throw new BadCredentialsException("Incorrect Email Id or password",e);
		}
		return ResponseEntity.ok(new AuthenticationResponse(jwtHandler.generateToken(loginform.getEmail())));
	}
}	
