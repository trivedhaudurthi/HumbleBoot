package com.northeastern.edu.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.transaction.annotation.Transactional;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northeastern.edu.SellerRepository;
import com.northeastern.edu.models.LoginForm;
import com.northeastern.edu.models.Product;
import com.northeastern.edu.models.Seller;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
@Import(ValidationAutoConfiguration.class)
public class SellerControllerTest {

	public SellerControllerTest() {
		// TODO Auto-generated constructor stub
	}
//	@Autowired
	private MockMvc mvc;	 
	@Autowired
    private WebApplicationContext context;
	@Autowired
	SellerRepository sellerRepo;
	@Autowired
	Validator validator;
	@BeforeEach
	public void setup()
	{
		mvc = MockMvcBuilders
		          .webAppContextSetup(context)
		          .apply(springSecurity())
		          .build();
	}

	@Test
	public void sellerSignUpTest() throws Exception {
		mvc.perform(post("/signup/seller").content("{\"name\":\"hello\",\"email\":\"hello@gmail.com\",\"password\":\"Test@1234\","
				+ "\"address\":\"1234 Boston\",\"zipcode\":12356}").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.message", is("success")));
//		Signup with same input should return bad request.
		mvc.perform(post("/signup/seller").content("{\"name\":\"hello\",\"email\":\"hello@gmail.com\",\"password\":\"Test@1234\","
				+ "\"address\":\"1234 Boston\",\"zipcode\":12356}").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//		Signup with invalid input
		Seller seller= new Seller();
		seller.setEmail("hellogh@.com");
		seller.setZipcode("012345");
		assertTrue(validator.validate(seller).size()>0);
	}
	@Test 
	public void authenticate() throws Exception{
		Seller seller= new Seller();
		seller.setName("seller");
		seller.setEmail("seller@humble.com");
		seller.setPassword("Test@1234");
		seller.setAddress("abcd, Boston");
		seller.setZipcode("12434");
		mvc.perform(post("/signup/seller").content(stringify(seller)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.message", is("success")));
		LoginForm form= new LoginForm();
		form.setEmail("seller@humble.com");
		
//		Invalid input should not grant access.
		mvc.perform(post("/authenticate").content(stringify(form)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
//		Invalid input should not grant access
		form.setPassword("abcd@1235");
		mvc.perform(post("/authenticate").content(stringify(form)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//		Valid input should return JWT token,
		form.setPassword("Test@1234");
		MvcResult result=mvc.perform(post("/authenticate").content(stringify(form)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		String responseString=result.getResponse().getContentAsString();
		String token=responseString.split(":")[1].split("\"")[1];
	}
	@Test
	public void productsTest() throws Exception {
//		Create seller
		Seller seller = createSeller();
		mvc.perform(post("/signup/seller").content(stringify(seller)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.message", is("success")));
		LoginForm form= new LoginForm();
		form.setEmail("seller@humble.com");
		form.setPassword("Test@1234");
		MvcResult result=mvc.perform(post("/authenticate").content(stringify(form)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		String responseString=result.getResponse().getContentAsString();
		String token=responseString.split(":")[1].split("\"")[1];
		Product product= new Product();
		product.setName("laptop");
		product.setPrice(1124.00);
		product.setQuantity(3);
		product.setType("tech");
//		Not authenticated should result in 403 status 
		mvc.perform(post("/seller/product").content(stringify(product)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
//		Authorized, should be successful
		mvc.perform(post("/seller/product").content(stringify(product)).header("Authorization", "Bearer "+token).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.message", is("successful")));
	}
//	Converts object to string
	public String stringify(Object obj) throws JsonProcessingException{
		return new ObjectMapper().writeValueAsString(obj);
	}
	public Seller createSeller()  {
		Seller seller= new Seller();
		seller.setName("seller");
		seller.setEmail("seller@humble.com");
		seller.setPassword("Test@1234");
		seller.setAddress("abcd, Boston");
		seller.setZipcode("12434");
		return seller;
	}
}
