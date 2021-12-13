package com.northeastern.edu;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.northeastern.edu.models.User;
import com.northeastern.edu.projections.CartProductView;
import com.northeastern.edu.projections.UserView;

@Component
public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByEmail(String email); 
//	@Query("select user.cart as cart from User user where user.email=?1")
//	public List<CartProductView> findUserCartByEmail(String email);
@Query("select s.id as id, s.name as name, s.email as email, s.zipcode as zipcode, s.address as address from User s "
		+ "where s.email= ?1 ")
public Optional<UserView> findByEmailForLimitedData(String email);
}
