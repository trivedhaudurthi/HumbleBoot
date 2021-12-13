package com.northeastern.edu.projections;

import java.util.List;

public interface SellerView {
	int getId();
	String getName();
	String getEmail();
	String getZipcode();
	String getAddress();
	List<SellerProductView> getProducts();
	interface SellerProductView{
		int getId();
		String getName();
		int getQuantity();
		double getPrice();
		String getDescription();
		String getType();
	}
}
