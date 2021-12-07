package com.northeastern.edu.CustomExceptions;

public interface ExceptionFactory {
    
    public RuntimeException getObject(String message);

    public RuntimeException getObject(String message,int status);
}
