package com.example.uptask.exceptions;

import lombok.Data;

@Data
public class InvalidJwtException extends RuntimeException {

    private Object errors;

    private String message;

    public InvalidJwtException (Object errors, String message) {
        this.errors = errors;
        this.message = message;
    }

}
