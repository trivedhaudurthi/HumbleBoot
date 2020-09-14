package com.northeastern.edu.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.northeastern.edu.MyUserDetailsService;
import com.northeastern.edu.UserPrincipal;
import com.northeastern.edu.UserRepository;
import com.northeastern.edu.models.Seller;
import com.northeastern.edu.models.User;
import com.northeastern.edu.util.JWTHandler;
import com.sun.net.httpserver.Filter.Chain;
@Component
public class UserAuthFilter extends OncePerRequestFilter {
	@Autowired
	JWTHandler jwtHandler;
	@Autowired
	MyUserDetailsService myUserDetailsService;
	public UserAuthFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authHeader= request.getHeader("Authorization");
//		System.out.println(authHeader);
		String username=null;
		String token=null;
		if(authHeader!=null&& authHeader.startsWith("Bearer ")) {
			token=authHeader.split(" ")[1];
			username= jwtHandler.extractSubject(authHeader.split(" ")[1]);
//			System.out.println(username);
//			System.out.println(token);
		}
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			if(request.getRequestURI().split("/")[1].equals("user")) {
				authenticate(User.class, "user", username, token, request);
			}
			else if(request.getRequestURI().split("/")[1].equals("seller")) {
//				System.out.println("seller");
				authenticate(Seller.class, "seller", username, token, request);
			}
		}
		filterChain.doFilter(request, response);
	}
	public <T> void authenticate(Class<T> cls,String sessionObjName,String username,String token,HttpServletRequest request) {
		UserPrincipal userPrincipal=(UserPrincipal) myUserDetailsService.loadUserByUsername(username);
		if(jwtHandler.validateToken(token,userPrincipal)&& userPrincipal.isOfType(cls)) {
			UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userPrincipal, null,userPrincipal.getAuthorities());
			authenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    request.getSession().setAttribute(sessionObjName, userPrincipal.getUsername());
		}
	}
}
