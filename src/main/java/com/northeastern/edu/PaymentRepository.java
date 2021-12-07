package com.northeastern.edu;

import com.northeastern.edu.models.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    
}
