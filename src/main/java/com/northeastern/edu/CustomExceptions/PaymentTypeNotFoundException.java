package com.northeastern.edu.CustomExceptions;

public class PaymentTypeNotFoundException extends RuntimeException {

    private int status=400;
    public PaymentTypeNotFoundException(String message) {
        super(message);
  
    }
    public PaymentTypeNotFoundException(String message, int status) {
        super(message);
        this.status=status;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
