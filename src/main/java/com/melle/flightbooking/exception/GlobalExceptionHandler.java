package com.melle.flightbooking.exception;

import jakarta.persistence.ElementCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(FlightNotFoundException e) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FlightAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle(FlightAlreadyExistsException e){
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EmailDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handle(EmailDoesNotExistException e){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handle(EmailAlreadyExistsException e){
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handle(InvalidCredentialsException e){
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
