package com.northeastern.edu;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.northeastern.edu.models.CartProduct;
import com.northeastern.edu.projections.CartProductView;
@Service
public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {

//	public Optional<CartProduct> findByProduct(int id);
	@Query("select cp.id as id, cp.product as product, cp.quantity as quantity from CartProduct cp where cp.id=?1")
	public CartProductView findByIdForLimitedData(int id);
//	Since we are selecting based on user id if we write cp.user=?1 then we should provide user object not id(integer).
	@Query("select cp.id as id, cp.product as product, cp.quantity as quantity from CartProduct cp where cp.user.id=?1")
	public List<CartProductView> findCartProductsOfUserForLimitedData(int id);
	@Query("select cp.id as id, cp.product as product, cp.quantity as quantity from CartProduct cp where cp.user.email=?1")
	public List<CartProductView> findCartProductsOfUserForLimitedDataByEmail(String email);
	@Query("from CartProduct cp where cp.user.id=?1")
	public List<CartProduct> findCartProductsOfUser(int id);
	@Query("from CartProduct cp where cp.user.email=?1")
	public List<CartProduct> findCartProductsOfUserByEmail(String email);
	@Query("from CartProduct cp where cp.user.id =?1 and cp.product.id=?2")
	public Optional<CartProduct> findByUserIdAndProductId(int uid,int pid);
	@Query("from CartProduct cp where cp.product.id=?1")
	public List<CartProduct> findByProductId(int id); 
	@Transactional
	@Modifying
	@Query ("delete from CartProduct cp where cp.product.id=?1")
	public void deleteAllByProductId(int id);
	@Transactional
	@Modifying
	@Query ("delete from CartProduct cp where cp.user.id=?1")
	public void deleteAllProductsByUserId(int id);

}
