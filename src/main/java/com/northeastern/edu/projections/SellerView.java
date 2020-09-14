package com.northeastern.edu.projections;

import java.util.List;

public interface SellerView {
	int getId();
	String getName();
	String getEmail();
	String getZipCode();
	String getAddress();
	List<SellerProductView> getProducts();
	interface SellerProductView{
		int getId();
		String getName();
	}
}
