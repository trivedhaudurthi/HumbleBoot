package com.northeastern.edu;

import com.northeastern.edu.models.CreditCard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer>   {
    
}
