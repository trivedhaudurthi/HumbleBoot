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

import com.northeastern.edu.SellerRepository;
import com.northeastern.edu.UserRepository;
import com.northeastern.edu.models.AuthenticationResponse;
import com.northeastern.edu.models.LoginForm;
import com.northeastern.edu.models.Response;
import com.northeastern.edu.models.Seller;
import com.northeastern.edu.models.User;
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
	public AuthenticationController() {
		// TODO Auto-generated constructor stub
	}
	@Transactional
	@PostMapping("/signup/user")
	public ResponseEntity<Response> signUp(@RequestBody User user) {
		validatePersons.validateForCreate(user);
		if(sellerRepo.findByEmail(user.getEmail())!=null) {
			return ResponseEntity.badRequest().build();
		}
		User savedUser= userRepo.save(user);
		return ResponseEntity.ok(Response.create("success"));
	}
	@Transactional
	@PostMapping("/signup/seller")
	public ResponseEntity<Response> signup(@RequestBody Seller seller){
			validatePersons.validateForCreate(seller);
			if(sellerRepo.findByEmail(seller.getEmail())!=null) {
				return ResponseEntity.badRequest().build();
			}
			Seller savedSeller= sellerRepo.save(seller);
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
			// TODO: handle exception
			throw new BadCredentialsException("Incorrect Email Id or password",e);
		}
		return ResponseEntity.ok(new AuthenticationResponse(jwtHandler.generateToken(loginform.getEmail())));
	}
}	
