package com.northeastern.edu.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class Seller extends Person {
	@OneToMany(mappedBy = "seller",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
	List<Product> products;
	public Seller() {
		// TODO Auto-generated constructor stub
	}
//	@JsonManagedReference
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}
