package com.northeastern.edu.controllers;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.northeastern.edu.CartProductRepository;
import com.northeastern.edu.CreditCardRepository;
import com.northeastern.edu.OrderRepository;
import com.northeastern.edu.PaymentRepository;
import com.northeastern.edu.PaypalRepository;
import com.northeastern.edu.ProductRepository;
import com.northeastern.edu.UserRepository;
import com.northeastern.edu.Command.CommandAPI;
import com.northeastern.edu.CustomExceptions.PaymentTypeNotFoundException;
import com.northeastern.edu.CustomExceptions.QuantityNotFoundExceptionFactory;
import com.northeastern.edu.Facade.DBOrderFacade;
import com.northeastern.edu.Facade.DBProductFacade;
import com.northeastern.edu.Facade.DBUserFacade;
import com.northeastern.edu.models.CartProduct;
import com.northeastern.edu.models.CartProductRequest;
import com.northeastern.edu.models.CreditCard;
import com.northeastern.edu.models.Payment;
import com.northeastern.edu.models.Paypal;
import com.northeastern.edu.models.Person;
import com.northeastern.edu.models.Product;
import com.northeastern.edu.models.User;
import com.northeastern.edu.models.UserOrder;
import com.northeastern.edu.projections.CartProductView;
import com.northeastern.edu.response.Response;
import com.northeastern.edu.response.ResponseAPI;
import com.northeastern.edu.response.ResponseBuilder;
import com.northeastern.edu.response.ResponseBuilderAPI;

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

	@Autowired
	private CreditCardRepository creditCardRepository;

	@Autowired
	private PaypalRepository paypalRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	private DBOrderFacade orderFacade;


	private DBUserFacade userFacade;


	private DBProductFacade productFacade;

	private CommandAPI notificationCommand;

	public UserController() {
		
	}

	

	@Autowired
	public UserController(DBOrderFacade orderFacade, DBUserFacade userFacade, DBProductFacade productFacade,CommandAPI command) {
        this.orderFacade = orderFacade;
        this.userFacade = userFacade;
        this.productFacade = productFacade;
		this.notificationCommand = command;
    }

    @Transactional
	@PostMapping("/cart")
	public ResponseEntity<ResponseAPI> addToCart(@RequestBody CartProductRequest reqBody,HttpServletRequest request){
		User user = userFacade.findUserByEmail((String)request.getSession().getAttribute("user"));
		List<CartProduct> cart= productFacade.findCartProductsByEmail(user.getEmail());
		Product product= productFacade.findProductById(reqBody.getId()).orElse(null);
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
				productFacade.saveProductToCart(cp);
				return ResponseEntity.ok(Response.create("added to cart"));
			}
		}
		CartProduct cartProduct= new CartProduct();
		cartProduct.setProduct(product);
		cartProduct.setQuantity(reqBody.getQuantity());
		cartProduct.setUser(user);
		productFacade.saveProductToCart(cartProduct);

		ResponseBuilderAPI rBuilder = new ResponseBuilder();
		rBuilder.addStatus(200);
		rBuilder.addMessage("added to cart");
		return ResponseEntity.ok(rBuilder.getResponse());
	}
	@Transactional
	@GetMapping("/cart")
	public ResponseEntity<List<CartProductView>> getUserCart(HttpServletRequest request){
		String email=(String)request.getSession().getAttribute("user");
		List<CartProductView> cart= productFacade.getCartProductsOfUserForLimitedDataByEmail(email);
		if(cart==null){
			cart= new ArrayList<>();
		}
		return ResponseEntity.ok(cart);
	}
	@Transactional
	@PatchMapping("/cart/{id}")
	public ResponseEntity<ResponseAPI> updateCart(@RequestBody CartProductRequest reqBody,@PathVariable int id, HttpServletRequest request){
//		User user = userRepo.findByEmail((String)request.getSession().getAttribute("user"));
		String email=(String)request.getSession().getAttribute("user");
		CartProduct cartproduct= productFacade.getCartProductById(id).orElse(null);
		if(cartproduct==null){
			return ResponseEntity.badRequest().body(Response.create("Item not found in your cart"));
		}
		if(!cartproduct.getUser().getEmail().equals(email)) {
			return ResponseEntity.badRequest().body(Response.create("Item not found in your cart"));
		}
		if(cartproduct.getProduct().getQuantity()<reqBody.getQuantity()){
			return ResponseEntity.badRequest().body(Response.create("product is not available for requested quantity."));
		}
		cartproduct.setQuantity(reqBody.getQuantity());
		productFacade.saveProductToCart(cartproduct);

		ResponseBuilderAPI rBuilder = new ResponseBuilder();
		rBuilder.addStatus(200);
		rBuilder.addMessage("Cart is being updated");
		return ResponseEntity.ok(rBuilder.getResponse());
	}
	@Transactional
	@DeleteMapping("/cart/{id}")
	public ResponseEntity<ResponseAPI> deleteItemFromCart(@PathVariable int id,HttpServletRequest request){
		String email= (String)request.getSession().getAttribute("user");
		productFacade.deleteFromCartById(id);
		return ResponseEntity.noContent().build();
	}


	public double processOrder(List<CartProduct> cart,Person user){
		double price = 0;
		Timestamp time = new Timestamp(System.currentTimeMillis());
		for(CartProduct cp:cart) {
			UserOrder order=new UserOrder();
			order.setProductId(cp.getProduct().getId());
			order.setProductName(cp.getProduct().getName());
			order.setSellerId(cp.getProduct().getSeller().getId());
			order.setSellerName(cp.getProduct().getSeller().getName());
			order.setUserId(user.getId());
			order.setUserName(user.getName());
			order.setQuantity(cp.getQuantity());
			order.setCreatedTime(time);
			price = price+(cp.getProduct().getPrice()*cp.getQuantity());
			orderFacade.saveOrder(order);
			cp.getProduct().setQuantity(cp.getProduct().getQuantity()-cp.getQuantity());
			productFacade.saveProduct(cp.getProduct());
		}
		return price;
	}

	public void verifyQuantity(List<CartProduct> cart){
		for(CartProduct cp:cart){
			if(cp.getQuantity()>cp.getProduct().getQuantity()){
				throw QuantityNotFoundExceptionFactory.getInstance().getObject("Product with id: "+cp.getProduct().getId()+" is not available in requested quantity", 400);
			}
		}
	}

	@Transactional
	@PostMapping("/order/creditcard")
	public ResponseEntity<ResponseAPI> orderWithCreditCard(HttpServletRequest request, @RequestBody CreditCard cardDetails){
		String email=(String)request.getSession().getAttribute("user");
		User user= userFacade.findUserByEmail(email);

		List<CartProduct> cart= cartRepo.findCartProductsOfUserByEmail(email);
		if(cart.size()==0) {
			return ResponseEntity.badRequest().body(Response.create("Please add products to cart"));
		}
		verifyQuantity(cart);
		double price=processOrder(cart, user);

		cardDetails.setPerson(user);
		cardDetails.setAmount(price);

		orderFacade.recordPayment(cardDetails);
		productFacade.deleteAllProductsFromCartByUserId(user.getId());
		notificationCommand.execute();

		ResponseBuilderAPI rBuilder = new ResponseBuilder();

		rBuilder.addStatus(200);
		rBuilder.addMessage("Placed your order. Thank you!");

		return ResponseEntity.ok(rBuilder.getResponse());
	}

	@Transactional
	@PostMapping("/order/paypal")
	public ResponseEntity<ResponseAPI> orderWithPayPal(HttpServletRequest request, @RequestBody Paypal paymentDetails){

		String email=(String)request.getSession().getAttribute("user");
		User user= userFacade.findUserByEmail(email);

		List<CartProduct> cart= cartRepo.findCartProductsOfUserByEmail(email);
		if(cart.size()==0) {
			return ResponseEntity.badRequest().body(Response.create("Please add products to cart"));
		}
		double price=processOrder(cart, user);

		paymentDetails.setToken("gdhvev76283bv63");
		paymentDetails.setAmount(price);
		
		orderFacade.recordPayment(paymentDetails);

		ResponseBuilderAPI rBuilder = new ResponseBuilder();

		rBuilder.addStatus(200);
		rBuilder.addMessage("Placed your order. Thank you!");

		return ResponseEntity.ok(rBuilder.getResponse());
	}

	@Transactional
	@GetMapping("/order")
	public ResponseEntity<List<UserOrder>> getOrders(HttpServletRequest request){
		String email=(String)request.getSession().getAttribute("user");
		User user= userFacade.findUserByEmail(email);
		List<UserOrder> orders= orderFacade.findOrdersByUserId(user.getId());
		return ResponseEntity.ok(orders);
	}
}
