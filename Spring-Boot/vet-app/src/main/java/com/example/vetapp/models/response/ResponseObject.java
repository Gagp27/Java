package com.example.vetapp.models.response;

import lombok.Data;

@Data
public class ResponseObject {

    private Object data;

    private Object errors;

    private String message;


    public ResponseObject() {
    }

    public ResponseObject(Object data, Object errors, String message) {
        this.data = data;
        this.errors = errors;
        this.message = message;
    }

    public ResponseObject(Object data, String message, Boolean success) {

        if(success) {
            this.data = data;
        } else {
            this.errors = data;
        }

        this.message = message;
    }


    public ResponseObject(String message) {
        this.message = message;
    }
}