package com.northeastern.edu.CustomExceptions;

public class DuplicateProductExceptionFactory implements ExceptionFactory {

    private static final DuplicateProductExceptionFactory obj = new DuplicateProductExceptionFactory();

    private DuplicateProductExceptionFactory(){

    }

    @Override
    public RuntimeException getObject(String message) {
        return new DuplicateProductException(message);
    }

    @Override
    public RuntimeException getObject(String message, int status) {
 
       return new DuplicateProductException(message, status);
        
    }

    public static DuplicateProductExceptionFactory getInstance(){
        return obj;
    }
    
}
