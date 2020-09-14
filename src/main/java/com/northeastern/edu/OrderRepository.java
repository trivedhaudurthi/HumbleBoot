package com.northeastern.edu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.northeastern.edu.models.UserOrder;

public interface OrderRepository extends JpaRepository<UserOrder, Integer> {
	public List<UserOrder> findByUserId(int id); 
	public List<UserOrder> findBySellerId(int id);
}
