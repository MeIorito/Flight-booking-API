package com.melle.flightbooking.exception;

public class EmailDoesNotExistException extends RuntimeException {
    public EmailDoesNotExistException(String message) {
        super(message);
    }
}
