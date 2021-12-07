package com.northeastern.edu.CustomExceptions;

public class QuantityNotSuffiecent extends RuntimeException{
    private int status = 400;

    

    public QuantityNotSuffiecent(String message) {
        super(message);
    }

    public QuantityNotSuffiecent(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
}
