package com.northeastern.edu.Facade;

import java.util.List;
import java.util.Optional;

import com.northeastern.edu.CartProductRepository;
import com.northeastern.edu.ProductRepository;
import com.northeastern.edu.models.CartProduct;
import com.northeastern.edu.models.Product;
import com.northeastern.edu.projections.CartProductView;
import com.northeastern.edu.projections.ProductView;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
public class DBProductFacade {
    
    private ProductRepository productRepository;
    private CartProductRepository cartProductRepository;

    public DBProductFacade(ProductRepository productRepository,CartProductRepository cartProductRepository) {
        this.productRepository = productRepository;
        this.cartProductRepository = cartProductRepository;
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public List<ProductView> searchByName(String name,Pageable pageable){
        return productRepository.searchByName(name, pageable);
    }
    public Optional<ProductView> findProductByName(String name){
        return productRepository.findByName(name);
    }



    public Optional<ProductView> findByIdForLimitedData(int id){
        return productRepository.findByIdForLimitedData(id);
    }

    public List<ProductView> findAllForLimitedData(){
        return productRepository.findAllForLimitedData();
    }

	public CartProductView getCartProductForLimitedData(int id){
        return cartProductRepository.findByIdForLimitedData(id);
    }

	public List<CartProductView> getCartProductsOfUserForLimitedData(int id){
        return cartProductRepository.findCartProductsOfUserForLimitedData(id);
    }

	public List<CartProductView> getCartProductsOfUserForLimitedDataByEmail(String email){
        return cartProductRepository.findCartProductsOfUserForLimitedDataByEmail(email);
    }

	public List<CartProduct> getProductsInCart(int id){
        return cartProductRepository.findCartProductsOfUser(id);
    }
	

	public List<CartProduct> getCartProductByProductId(int id){
        return cartProductRepository.findByProductId(id);
    }

    public Optional<CartProduct> getCartProductById(int id){
        return cartProductRepository.findById(id); 
    }

	public void deleteAllByProductId(int id){
         cartProductRepository.deleteAllByProductId(id);
    }

	public void deleteAllProductsFromCartByUserId(int id){
        cartProductRepository.deleteAllProductsByUserId(id);
    }

    public Optional<Product> findProductById(int id){
        return productRepository.findById(id);
    }

    public void deleteProduct(Product product){
        productRepository.delete(product);
    }

    public void deleteFromCartById(int id   ){
        cartProductRepository.deleteById(id);
    }

    public Optional<CartProduct> getCartProductByUserIdAndProductId(int uid,int pid){
        return cartProductRepository.findByUserIdAndProductId(uid, pid);
    }

    public List<CartProduct> findCartProductsByEmail(String email){
        return cartProductRepository.findCartProductsOfUserByEmail(email);
    }

    public CartProduct saveProductToCart(CartProduct product){
        return cartProductRepository.save(product);
    }
}
