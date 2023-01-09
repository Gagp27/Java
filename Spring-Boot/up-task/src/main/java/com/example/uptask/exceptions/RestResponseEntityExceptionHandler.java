package com.example.uptask.exceptions;

import com.example.uptask.model.dto.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    ResponseEntity<ResponseObject> apiException(ApiException exception) {
        ResponseObject response = new ResponseObject(null, "Internal server error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    ResponseEntity<ResponseObject> entityNotFoundException(EntityNotFoundException exception) {
        ResponseObject response = new ResponseObject(null, "Not found", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = InvalidJwtException.class)
    ResponseEntity<ResponseObject> invalidJwtException(InvalidJwtException exception) {
        ResponseObject response = new ResponseObject(null, exception.getErrors(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = MailerNotSendException.class)
    ResponseEntity<ResponseObject> mailerNotSendException(MailerNotSendException exception) {
        ResponseObject response = new ResponseObject(null, "Internal server error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ResponseObject> accessDeniedException(AccessDeniedException exception) {
        ResponseObject response = new ResponseObject(null, "Access denied", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}