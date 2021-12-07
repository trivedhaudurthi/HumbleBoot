package com.northeastern.edu.CustomExceptions;

public class DuplicateProductException extends RuntimeException{
    
    private int status = 400;


    public DuplicateProductException(String message) {
        super(message);
    }
    public DuplicateProductException(String message, int status) {
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
