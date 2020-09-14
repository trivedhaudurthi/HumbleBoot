package com.northeastern.edu.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.northeastern.edu.CartProductRepository;
import com.northeastern.edu.OrderRepository;
import com.northeastern.edu.ProductRepository;
import com.northeastern.edu.SellerRepository;
import com.northeastern.edu.UserRepository;
import com.northeastern.edu.models.CartProduct;
import com.northeastern.edu.models.Product;
import com.northeastern.edu.models.Response;
import com.northeastern.edu.models.Seller;
import com.northeastern.edu.models.User;
import com.northeastern.edu.models.UserOrder;
import com.northeastern.edu.projections.ProductView;
import com.northeastern.edu.projections.SellerView;
import com.northeastern.edu.validators.ValidatePersonsWithGroups;

@RestController
@RequestMapping("/seller")
public class SellerController {

	@Autowired
	private SellerRepository sellerRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private CartProductRepository cartRepo;
	@Autowired
	private ValidatePersonsWithGroups validatePerson;
	@Autowired
	private OrderRepository orderRepo;
	public SellerController() {
		// TODO Auto-generated constructor stub
	}
	@Transactional
	@GetMapping("/")
	public ResponseEntity<SellerView> getSeller(HttpServletRequest request){
		String username=(String)request.getSession().getAttribute("seller");
		return ResponseEntity.ok(sellerRepo.findByEmailForLimitedData(username));
	}
	@Transactional
	@PatchMapping("/")
	public ResponseEntity<Response> updateSeller(@RequestBody Seller seller,HttpServletRequest request){
		String username=(String)request.getSession().getAttribute("seller");
//		System.out.println(username);
		Seller sellerDB= sellerRepo.findByEmail(username);
		validatePerson.validateForUpdate(seller);
		sellerDB.setAddress(seller.getAddress());
		sellerDB.setEmail(seller.getEmail());
		sellerDB.setName(seller.getName());
		sellerDB.setZipcode(seller.getZipcode());
		sellerRepo.save(sellerDB);
		return ResponseEntity.ok(Response.create("success"));
	}
	@Transactional
	@DeleteMapping("/")
	public ResponseEntity<Response> deleteSeller(HttpServletRequest request){
		String username=(String)request.getSession().getAttribute("seller");
		Seller seller = sellerRepo.findByEmail(username);
		sellerRepo.delete(seller);
		return ResponseEntity.noContent().build();
	}
	@Transactional
	@PostMapping("/product")
	public ResponseEntity<Response> addProduct(@Valid @RequestBody Product product,HttpServletRequest request){
		String username= (String)request.getSession().getAttribute("seller");
		Seller seller= sellerRepo.findByEmail(username);
		product.setSeller(seller);
		product=prodRepo.save(product);
		System.out.println(seller.getProducts());
		seller.getProducts().add(product);
//		seller=sellerRepo.save(seller);
		return ResponseEntity.ok(Response.create("successful"));	
	}
	
	@Transactional
	@PatchMapping("/product/{id}")
	public ResponseEntity<Response> updateProduct(@RequestBody Product product,HttpServletRequest request,@PathVariable int id){
		Product productDB= prodRepo.findById(id).orElse(null);
		String username=(String)request.getSession().getAttribute("seller");
		if(productDB==null||!username.equals(productDB.getSeller().getEmail())) {
			ResponseEntity.badRequest().body("Invalid Product Id");
		}
		productDB.setName(product.getName());
		productDB.setDescription(product.getDescription());
		productDB.setPrice(product.getPrice());
		productDB.setQuantity(product.getQuantity());
		productDB.setType(product.getType());
		prodRepo.save(productDB);
		return ResponseEntity.ok(Response.create("successfully updated"));
	}
	@Transactional
	@DeleteMapping("/product/{id}")
	public ResponseEntity<Response> deleteProduct(@PathVariable int id,HttpServletRequest request){
		Product product= prodRepo.findById(id).orElse(null);
		if(product==null) {
			return ResponseEntity.badRequest().body(Response.create("Invalid ID"));
		}
		cartRepo.deleteAllByProductId(id);
		prodRepo.delete(product);
		return ResponseEntity.noContent().build();
	}
	@Transactional
	@PostMapping("/order/{id}")
	public ResponseEntity<Response> processOrder(@PathVariable int id, @RequestParam boolean status){
		UserOrder order=orderRepo.findById(id).orElse(null);
		if(order==null||order.isProcessed()	) {
			return ResponseEntity.badRequest().body(Response.create("Order does not exist"));
		}
		User user= userRepo.findById(order.getUserId()).orElse(null);
		if(user==null) {
			return ResponseEntity.badRequest().body(Response.create("invalid order"));
		}
		if(status) {
			CartProduct cartprod= cartRepo.findByUserIdAndProductId(order.getUserId(), order.getProductId()).orElse(null);
			if(cartprod==null) {
				order.setProcessed(true);
				orderRepo.save(order);
				return ResponseEntity.badRequest().body(Response.create("Requested product is not available at this moment"));
			}
			Product product= cartprod.getProduct();
			if(product.getQuantity()-order.getQuantity()<0) {
				order.setProcessed(true);
				orderRepo.save(order);
				return ResponseEntity.badRequest().body(Response.create("item is not available in requested quantity"));
			}
			cartRepo.delete(cartprod);
			product.setQuantity(product.getQuantity()-order.getQuantity());
			prodRepo.save(product);	
		}
		order.setProcessed(true);
		order.setAccepted(status);
		orderRepo.save(order);
		return ResponseEntity.ok(Response.create("processing completed"));
	}
	@Transactional
	@GetMapping("/order")
	public ResponseEntity<List<UserOrder>> getOrders(HttpServletRequest request){
		String email=(String)request.getSession().getAttribute("seller");
		Seller seller= sellerRepo.findByEmail(email);
		List<UserOrder> orders= orderRepo.findBySellerId(seller.getId());
		return ResponseEntity.ok(orders);
	}
}
