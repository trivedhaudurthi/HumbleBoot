package com.northeastern.edu;

import com.northeastern.edu.models.Paypal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PaypalRepository extends JpaRepository<Paypal,Integer>{
    
}
