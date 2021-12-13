package com.northeastern.edu.CustomExceptions;

public class UserNotFoundException extends RuntimeException {
    private int status=400;
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(String message, int status) {
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
