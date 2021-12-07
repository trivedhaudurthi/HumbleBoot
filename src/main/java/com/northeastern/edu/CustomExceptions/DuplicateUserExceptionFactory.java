package com.northeastern.edu.CustomExceptions;

public class DuplicateUserExceptionFactory implements ExceptionFactory {


    private static final DuplicateUserExceptionFactory obj = new DuplicateUserExceptionFactory();

    private DuplicateUserExceptionFactory(){

    }

    @Override
    public RuntimeException getObject(String message) {

        return new DuplicateUserException(message);
    }

    @Override
    public RuntimeException getObject(String message, int status) {

        return new DuplicateUserException(message, status);
    }
    
    public static ExceptionFactory getInstance(){
        return obj;
    }



}
