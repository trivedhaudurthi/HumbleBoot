package com.northeastern.edu.response;

public class ResponseBuilder implements ResponseBuilderAPI {

    private ResponseAPI response ;

    

    public ResponseBuilder() {
        this.response = new Response();
    }

    @Override
    public void addMessage(String message) {
       
        this.response.setMessage(message);
        
    }

    @Override
    public void addStatus(int statusCode) {

        this.response.setStatus(statusCode);
        
    }

    @Override
    public ResponseAPI getResponse() {
        return this.response;
    }
    
}
