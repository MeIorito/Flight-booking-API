package com.melle.flightbooking.exception;

public class IdDoesNotExistException extends RuntimeException {
    public IdDoesNotExistException(String message) {
        super(message);
    }
}
