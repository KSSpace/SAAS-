package com.saas.user.exception;

public class RequestException extends RuntimeException{
    public RequestException(String message) {
        super(message);
    }
}
