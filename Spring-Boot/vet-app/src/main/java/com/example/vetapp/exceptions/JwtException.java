package com.example.vetapp.exceptions;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
