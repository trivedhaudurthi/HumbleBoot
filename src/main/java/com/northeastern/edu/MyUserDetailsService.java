package com.northeastern.edu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.northeastern.edu.models.Person;
@Service
public class MyUserDetailsService implements UserDetailsService  {

	@Autowired
	PersonRepository repo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Person user=repo.findByEmail(username);
		if(user==null)
			throw new UsernameNotFoundException("User does not exist");
		return new UserPrincipal(user);
	}

}
