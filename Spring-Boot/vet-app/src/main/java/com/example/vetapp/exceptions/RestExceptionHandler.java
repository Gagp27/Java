package com.example.vetapp.exceptions;

import com.example.vetapp.models.response.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = MailerException.class)
    ResponseEntity<ResponseObject> mailerException(MailerException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject(exception.getMessage()));
    }

    @ExceptionHandler(value = NotFoundException.class)
    ResponseEntity<ResponseObject> notFoundException(NotFoundException exception) {
        ResponseObject response = new ResponseObject(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = JwtException.class)
    ResponseEntity<ResponseObject> jwtException(JwtException exception) {
        ResponseObject response = new ResponseObject(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(value = ApiException.class)
    ResponseEntity<ResponseObject> apiException(ApiException exception) {
        ResponseObject response = new ResponseObject(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

