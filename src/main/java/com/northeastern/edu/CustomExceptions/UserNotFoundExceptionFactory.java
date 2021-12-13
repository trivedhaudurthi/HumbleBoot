package com.northeastern.edu.CustomExceptions;

public class UserNotFoundExceptionFactory implements ExceptionFactory{
    private static ExceptionFactory exceptionFactory = null;
    @Override
    public RuntimeException getObject(String message) {
        return new UserNotFoundException(message);
    }

    @Override
    public RuntimeException getObject(String message, int status) {
        
        return new UserNotFoundException(message, status);
    }

    public static ExceptionFactory getInstance(){
        if(exceptionFactory==null){
            exceptionFactory = new UserNotFoundExceptionFactory();
        }
        return exceptionFactory;
    }
    
}
