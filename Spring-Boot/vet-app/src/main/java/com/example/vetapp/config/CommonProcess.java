package com.example.vetapp.config;

import com.example.vetapp.models.response.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public class CommonProcess {

    /**
     * Process to valid a Binding result object and build a response
     */

    public static ResponseEntity<ResponseObject> validBindingResult(Object data, BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        if(result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(data, errors, "Failed Validation"));
        }

        return null;
    }
}
