package com.e_commerce.users.handlers;

import com.e_commerce.users.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handlerBadRequestException(BadRequestException bre) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bre.getMessage());
    }
}
