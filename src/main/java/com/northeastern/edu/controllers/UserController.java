package com.northeastern.edu.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.northeastern.edu.CartProductRepository;
import com.northeastern.edu.OrderRepository;
import com.northeastern.edu.ProductRepository;
import com.northeastern.edu.UserRepository;
import com.northeastern.edu.models.CartProduct;
import com.northeastern.edu.models.CartProductRequest;
import com.northeastern.edu.models.Product;
import com.northeastern.edu.models.Response;
import com.northeastern.edu.models.User;
import com.northeastern.edu.models.UserOrder;
import com.northeastern.edu.projections.CartProductView;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	ProductRepository prodRepo;
	@Autowired
	CartProductRepository cartRepo;
	@Autowired
	OrderRepository orderRepo;
	public UserController() {
		// TODO Auto-generated constructor stub
	}
	@Transactional
	@PostMapping("/cart")
	public ResponseEntity<Response> addToCart(@RequestBody CartProductRequest reqBody,HttpServletRequest request){
		User user = userRepo.findByEmail((String)request.getSession().getAttribute("user"));
		List<CartProduct> cart= cartRepo.findCartProductsOfUserByEmail(user.getEmail());
		Product product= prodRepo.findById(reqBody.getId()).orElse(null);
		if(product==null) {
			return ResponseEntity.badRequest().body(Response.create("product does not exist"));
		}
		else if(product.getQuantity()<reqBody.getQuantity()){
			return ResponseEntity.badRequest().body(Response.create("product is not available for requested quantity."));
		}
		for(CartProduct cp: cart) {
			if(cp.getProduct().getId()==reqBody.getId()) {
				if(product.getQuantity()<cp.getQuantity()+reqBody.getQuantity()){
					return ResponseEntity.badRequest().body(Response.create("product is not available for requested quantity."));
				}
				cp.setQuantity(cp.getQuantity()+reqBody.getQuantity());
				cartRepo.save(cp);
				return ResponseEntity.ok(Response.create("added to cart"));
			}
		}
		CartProduct cartProduct= new CartProduct();
		cartProduct.setProduct(product);
		cartProduct.setQuantity(reqBody.getQuantity());
		cartProduct.setUser(user);
		cartRepo.save(cartProduct);
		return ResponseEntity.ok(Response.create("added to cart"));
	}
	@Transactional
	@GetMapping("/cart")
	public ResponseEntity<List<CartProductView>> getUserCart(HttpServletRequest request){
		String email=(String)request.getSession().getAttribute("user");
		List<CartProductView> cart= cartRepo.findCartProductsOfUserForLimitedDataByEmail(email);
		return ResponseEntity.ok(cart);
	}
	@Transactional
	@PatchMapping("/cart/{id}")
	public ResponseEntity<Response> updateCart(@RequestBody CartProductRequest reqBody,@PathVariable int id, HttpServletRequest request){
//		User user = userRepo.findByEmail((String)request.getSession().getAttribute("user"));
		String email=(String)request.getSession().getAttribute("user");
		CartProduct cartproduct= cartRepo.findById(id).orElse(null);
		if(!cartproduct.getUser().getEmail().equals(email)) {
			return ResponseEntity.badRequest().body(Response.create("Item not found in your cart"));
		}
		if(cartproduct.getProduct().getQuantity()<cartproduct.getQuantity()+reqBody.getQuantity()){
			return ResponseEntity.badRequest().body(Response.create("product is not available for requested quantity."));
		}
		cartproduct.setQuantity(cartproduct.getQuantity()+reqBody.getQuantity());
		cartRepo.save(cartproduct);
		return ResponseEntity.ok(Response.create("added to cart"));
	}
	@Transactional
	@DeleteMapping("/cart/{id}")
	public ResponseEntity<Response> deleteItemFromCart(@PathVariable int id,HttpServletRequest request){
		String email= (String)request.getSession().getAttribute("user");
		cartRepo.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	@Transactional
	@PostMapping("/order")
	public ResponseEntity<Response> order(HttpServletRequest request){
		String email=(String)request.getSession().getAttribute("user");
		User user= userRepo.findByEmail(email);
		List<CartProduct> cart= cartRepo.findCartProductsOfUserByEmail(email);
		if(cart.size()==0) {
			return ResponseEntity.badRequest().body(Response.create("Please add products to cart"));
		}
		for(CartProduct cp:cart) {
			UserOrder order=new UserOrder();
			order.setProductId(cp.getProduct().getId());
			order.setProductName(cp.getProduct().getName());
			order.setSellerId(cp.getProduct().getSeller().getId());
			order.setSellerName(cp.getProduct().getSeller().getName());
			order.setUserId(user.getId());
			order.setUserName(user.getName());
			order.setQuantity(cp.getQuantity());
			orderRepo.save(order);
		}
		return ResponseEntity.ok(Response.create("Placed your order. Thank you!"));
	}
	@Transactional
	@GetMapping("/order")
	public ResponseEntity<List<UserOrder>> getOrders(HttpServletRequest request){
		String email=(String)request.getSession().getAttribute("user");
		User user= userRepo.findByEmail(email);
		List<UserOrder> orders= orderRepo.findByUserId(user.getId());
		return ResponseEntity.ok(orders);
	}
}
