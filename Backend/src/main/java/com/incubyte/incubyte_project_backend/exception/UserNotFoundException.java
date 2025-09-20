package com.incubyte.incubyte_project_backend.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Automatically maps to HTTP 409
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User Not Found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}