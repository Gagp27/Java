package com.example.uptask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject {

    private Object data;
    private Object errors;
    private String message;

    public ResponseObject(Object data, String message) {
        this.data = data;
        this.message = message;
    }

}