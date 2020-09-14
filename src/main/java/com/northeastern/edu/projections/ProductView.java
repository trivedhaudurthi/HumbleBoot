package com.northeastern.edu.projections;


public interface ProductView {
	int getId();
	String getName();
	String getDescription();
	ProductSellerView getSeller();
	String getPrice();
	String getQuantity();
	String getType();
	String getPhoto();
}
