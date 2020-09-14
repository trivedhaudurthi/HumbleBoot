package com.northeastern.edu.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.northeastern.edu.UserRepository;
import com.northeastern.edu.models.User;
//@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private UserRepository userRepo;
	public UserControllerTest() {
		// TODO Auto-generated constructor stub
	}
	@Test
	public void userSignupTest() throws Exception {
//		String reqBody= String.format("{\"name\" : %s, \"email\" : %s, \"password\" : %s, \"address\" : %s, \"zipcode\" : %s ", "\"hello\"","\"hello@gmail.com\"",
//				"\"Test@1234\"","\"1244 Boston\"","02115}");
		mvc.perform(post("/signup/user").content("{\"name\":\"hello\",\"email\":\"hello@gmail.com\",\"password\":\"Test@1234\","
				+ "\"address\":\"1234 Boston\",\"zipcode\":12356}").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.message", is("success")));
	}
}
