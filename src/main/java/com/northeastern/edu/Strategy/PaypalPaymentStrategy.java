package com.northeastern.edu.Strategy;

import com.northeastern.edu.models.User;

public class PaypalPaymentStrategy implements PaymentStrategyAPI{
    private static double processing_fee=0.99;
    private static double tax= 2;
    @Override
    public double charge(double amount, User user) {
        return (amount+amount*(tax/100))+processing_fee;
    }
    
}
