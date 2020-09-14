package com.northeastern.edu;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.northeastern.edu.models.Product;
import com.northeastern.edu.projections.ProductView;
@Component
public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query("select prod.id as id, prod.name as name, prod.description as description, prod.seller as seller, prod.price as price, prod.quantity as quantity, "
			+ "prod.type as type, prod.photo as photo from Product prod where prod.name like ?1%")
	public List<ProductView> searchByName(String name,Pageable pageable);
	public Optional<ProductView> findByName(String name);
	@Query("select prod.id as id,prod.name as name, prod.description as description, prod.seller as seller, prod.price as price, prod.quantity as quantity, "
			+ "prod.type as type, prod.photo as photo from Product prod where prod.id = ?1")
	public Optional<ProductView> findByIdForLimitedData(int id);
	@Query("select prod.id as id,prod.name as name, prod.description as description, prod.seller as seller, prod.price as price, prod.quantity as quantity, "
			+ "prod.type as type, prod.photo as photo from Product prod")
	public List<ProductView> findAllForLimitedData();
//	public List<ProductView> 
}
