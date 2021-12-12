package com.northeastern.edu.Strategy;

import com.northeastern.edu.models.User;

public class CreditCardPaymentStrategy implements PaymentStrategyAPI {

    private static double processing_fee=1.99;

    @Override
    public double charge(double amount, User user) {
        // TODO Auto-generated method stub
        if(amount>40){
            amount+=processing_fee;
        }
        return amount;
    }
    
}
