package com.example.uptask.exceptions;

public class MailerNotSendException extends RuntimeException {

    public MailerNotSendException(String message) {
        super(message);
    }
}
