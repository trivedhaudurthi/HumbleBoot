package com.northeastern.edu.CustomExceptions;

public class DuplicateUserException extends RuntimeException {

    private int status=400;
    public DuplicateUserException(String message) {
        super(message);
    }
    public DuplicateUserException(String message, int status) {
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
