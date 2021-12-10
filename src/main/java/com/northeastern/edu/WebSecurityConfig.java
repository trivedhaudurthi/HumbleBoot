package com.northeastern.edu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.northeastern.edu.filter.UserAuthFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserAuthFilter userfilter;
	@Autowired
	private UserDetailsService userDetailsService;
	public WebSecurityConfig() {
		// TODO Auto-generated constructor stub
	}

	public WebSecurityConfig(boolean disableDefaults) {
		super(disableDefaults);
		// TODO Auto-generated constructor stub
	}

	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//		authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return authenticationProvider;
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/user/**").authenticated().antMatchers("/seller/**").authenticated().anyRequest().permitAll().and().exceptionHandling()
			.and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(userfilter, UsernamePasswordAuthenticationFilter.class);
		http.cors();
	}
//	@Bean
//	public FilterRegistrationBean<UserAuthFilter> userAuthFilter(){
//		FilterRegistrationBean<UserAuthFilter> registrationBean = new FilterRegistrationBean<>();
//	    registrationBean.setFilter(new UserAuthFilter());
//	    registrationBean.addUrlPatterns("/user/**");
//	    return registrationBean;
//	}
//	@Bean
//	public FilterRegistrationBean<SellerAuthFilter> sellerAuthFilter(){
//		FilterRegistrationBean<SellerAuthFilter> registrationBean = new FilterRegistrationBean<>();
//	    registrationBean.setFilter(new SellerAuthFilter());
//	    registrationBean.addUrlPatterns("/seller/**");
//	    return registrationBean;
//	}
}
