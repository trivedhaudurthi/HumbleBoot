package com.northeastern.edu;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.northeastern.edu.models.UserOrder;

public interface OrderRepository extends JpaRepository<UserOrder, Integer> {
	public List<UserOrder> findByUserId(int id); 
	public List<UserOrder> findBySellerId(int id);
	@Query("from UserOrder ord where ord.userId=?1")
	public List<UserOrder> findLatestUserOrderByUserId(int id,Pageable pageable);
	@Query("from UserOrder ord where ord.createdTime=?1 and ord.userId=?2")
	public List<UserOrder> findLatestUserOrders(Timestamp time, int id);
	// public Optional<UserOrder> findFirstByOrderByUserName

}
