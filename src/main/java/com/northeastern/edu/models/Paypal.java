package com.northeastern.edu.models;

import javax.persistence.Entity;

@Entity
public class Paypal extends Payment {
    
    private String token;

    public Paypal(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    
}
