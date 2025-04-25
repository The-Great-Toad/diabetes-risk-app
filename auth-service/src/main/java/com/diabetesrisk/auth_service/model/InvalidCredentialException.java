package com.diabetesrisk.auth_service.model;

public class InvalidCredentialException extends RuntimeException {

//    private final String message = "Invalid credentials provided";

    public InvalidCredentialException() {
        super("Invalid credentials provided");
    }
}
