package com.exemple.subscriptionloyalty.Config;

import com.exemple.subscriptionloyalty.Exception.EurekaUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EurekaUnavailableException.class)
    public ResponseEntity<String> handleEurekaUnavailable(EurekaUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(exception.getMessage());
    }
}
