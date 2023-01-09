package com.example.uptask.exceptions;


import java.util.function.Supplier;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException() {}

}
