package com.northeastern.edu.Strategy;

import com.northeastern.edu.models.User;

public interface PaymentStrategyAPI {
    public double charge(double amount,User user);
}
