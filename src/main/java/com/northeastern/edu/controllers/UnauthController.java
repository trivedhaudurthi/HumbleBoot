package com.northeastern.edu.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.northeastern.edu.ProductRepository;
import com.northeastern.edu.SellerRepository;
import com.northeastern.edu.Facade.DBProductFacade;
import com.northeastern.edu.Facade.DBUserFacade;
import com.northeastern.edu.models.Product;
import com.northeastern.edu.models.Seller;
import com.northeastern.edu.models.User;
import com.northeastern.edu.projections.ProductView;
import com.northeastern.edu.response.ResponseAPI;
import com.northeastern.edu.response.ResponseBuilder;
import com.northeastern.edu.response.ResponseBuilderAPI;
import com.northeastern.edu.util.JWTHandler;


@RestController
public class UnauthController {
	@Autowired
	private DBProductFacade productFacade;
	@Autowired
	private DBUserFacade userFacade;

	@Autowired
	private JWTHandler jwtHandler;

	public UnauthController() {
		
	}

	@Transactional
	@GetMapping("/info/seller/{id}")
	public ResponseEntity<Seller> getSeller(@PathVariable int id){
		Seller seller= userFacade.findSellerById(id).orElse(null);
		if(seller==null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(seller);
	}
	@Transactional
	@GetMapping("/search/product/{name}")
	public ResponseEntity<List<ProductView>> searchProduct(@PathVariable String name,@RequestParam int page,@RequestParam String sort){
		Pageable pageable= PageRequest.of(page-1, 3,Sort.by(sort));
		List<ProductView> produts= productFacade.searchByName(name,pageable);
		return ResponseEntity.ok(produts);
	}
	@Transactional
	@PostMapping("/validate")
	public ResponseEntity<ResponseAPI> vaildate(HttpServletRequest request){
		ResponseBuilderAPI rb = new ResponseBuilder();
		try {
			String authHeader= request.getHeader("Authorization");
			String username=null;
			String token=null;
			if(authHeader!=null&& authHeader.startsWith("Bearer ")) {
				token=authHeader.split(" ")[1];
				username= jwtHandler.extractSubject(authHeader.split(" ")[1]);
			}
			User user = userFacade.findUserByEmail(username);
			Seller seller = userFacade.findSellerByEmail(username);
			if(user==null&&seller==null){
				throw new RuntimeException("User not found");
			}
			String role = seller==null?"user":"seller";
			rb.addMessage(role);
			rb.addStatus(200);
			return ResponseEntity.ok().body(rb.getResponse());
		} catch (Exception e) {
			rb.addMessage("Invalid token");
			rb.addStatus(403);
			return ResponseEntity.status(403).body(rb.getResponse());
		}

	}
}
