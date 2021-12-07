package com.northeastern.edu.CustomExceptions;

public class PaymentTypeNotFoundExceptionFactory implements ExceptionFactory{

    private static final PaymentTypeNotFoundExceptionFactory obj = new PaymentTypeNotFoundExceptionFactory();

    private PaymentTypeNotFoundExceptionFactory(){

    }
    @Override
    public RuntimeException getObject(String message) {

        return new PaymentTypeNotFoundException(message);
    }

    @Override
    public RuntimeException getObject(String message, int status) {
        
        return new PaymentTypeNotFoundException(message,status);
    }

    public static ExceptionFactory getInsatane(){
        return obj;
    }
    
}
