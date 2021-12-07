package com.northeastern.edu.response;

public interface ResponseBuilderAPI {

    public void addMessage(String message);

    public void addStatus(int statusCode);

    public ResponseAPI getResponse();
}
