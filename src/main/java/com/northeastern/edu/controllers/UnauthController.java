package com.northeastern.edu.controllers;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.northeastern.edu.ProductRepository;
import com.northeastern.edu.SellerRepository;
import com.northeastern.edu.models.Product;
import com.northeastern.edu.models.Seller;
import com.northeastern.edu.projections.ProductView;


@RestController
public class UnauthController {
	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	SellerRepository sellerRepo;
	public UnauthController() {
		// TODO Auto-generated constructor stub
	}

	@Transactional
	@GetMapping("/info/seller/{id}")
	public ResponseEntity<Seller> getSeller(@PathVariable int id){
		Seller seller= sellerRepo.findById(id).orElse(null);
		if(seller==null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(seller);
	}
	@Transactional
	@GetMapping("/search/product/{name}")
	public ResponseEntity<List<ProductView>> searchProduct(@PathVariable String name,@RequestParam int page,@RequestParam String sort){
		Pageable pageable= PageRequest.of(page-1, 3,Sort.by(sort));
		List<ProductView> produts= prodRepo.searchByName(name,pageable);
		return ResponseEntity.ok(produts);
	}
}
