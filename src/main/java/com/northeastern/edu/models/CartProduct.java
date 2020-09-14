package com.northeastern.edu.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;

@Entity
public class CartProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@OneToOne
	private Product product;
	@Min(1)
	private int quantity;
	@ManyToOne
	private User user;
	public CartProduct() {
		// TODO Auto-generated constructor stub
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quntity) {
		this.quantity = quntity;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj==this)
			return true;
		if(!(obj instanceof CartProduct ))
			return false;
		else {
			CartProduct o= (CartProduct)obj;
			return this.product.getId()==o.getProduct().getId();
		}
	}
	
}
