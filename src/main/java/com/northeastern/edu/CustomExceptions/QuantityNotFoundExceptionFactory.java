package com.northeastern.edu.CustomExceptions;

public class QuantityNotFoundExceptionFactory implements ExceptionFactory{

    private static QuantityNotFoundExceptionFactory obj;

    

    private QuantityNotFoundExceptionFactory() {
    }

    @Override
    public RuntimeException getObject(String message) {
        return new QuantityNotSuffiecent(message);
    }

    @Override
    public RuntimeException getObject(String message, int status) {
        return new QuantityNotSuffiecent(message, status);
    }
    
    public static ExceptionFactory getInstance(){
        if(obj==null){
           obj = new QuantityNotFoundExceptionFactory();
        }
        return obj;
    }
}
