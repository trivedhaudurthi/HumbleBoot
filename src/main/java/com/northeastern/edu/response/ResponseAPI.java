package com.northeastern.edu.response;

public interface ResponseAPI {
    
    public int getStatus();

    public String getMessage();

    public void setMessage(String message);

    public void setStatus(int statusCode);
}
