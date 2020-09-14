package com.northeastern.edu.controllers;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.northeastern.edu.ProductRepository;
import com.northeastern.edu.models.Product;
import com.northeastern.edu.projections.ProductView;

@RestController
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductRepository prodRepo;
	public ProductController() {
		// TODO Auto-generated constructor stub
	}
	@Transactional
	@GetMapping("/{id}")
	public ResponseEntity<ProductView> getProductById(@PathVariable int id){
		ProductView product= prodRepo.findByIdForLimitedData(id).orElse(null);
		if(product==null)
			return ResponseEntity.notFound().build();
//		System.out.println(product.getName());
		return ResponseEntity.ok(product);
	}
	@Transactional
	@GetMapping("")
	public ResponseEntity<List<ProductView>> getAllProducts(){
		List<ProductView> products= prodRepo.findAllForLimitedData();
		return ResponseEntity.ok(products);
	}
}
