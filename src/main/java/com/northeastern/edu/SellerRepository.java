package com.northeastern.edu;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.northeastern.edu.models.Seller;
import com.northeastern.edu.projections.SellerView;
@Component
public interface SellerRepository extends JpaRepository<Seller, Integer> {
	public Seller findByEmail(String email);
	@Query("select s.id as id, s.name as name, s.email as email, s.zipcode as zipcode, s.address as address, s.products as products from Seller s "
			+ "where s.id= ?1 ")
	public Optional<SellerView> findByIdForLimitedData(int id);
	@Query("select s.id as id, s.name as name, s.email as email, s.zipcode as zipcode, s.address as address, s.products as products from Seller s "
			+ "where s.email= ?1 ")
	public Optional<SellerView> findByEmailForLimitedData(String email);
}
