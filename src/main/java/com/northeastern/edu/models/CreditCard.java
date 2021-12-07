package com.northeastern.edu.models;

import javax.persistence.Entity;

@Entity
public class CreditCard extends Payment {
 
    private String cardNumber;

    private String nameOnCard;

    private int cvv;

    

    public CreditCard() {
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    
}
