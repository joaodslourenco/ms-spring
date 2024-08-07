package com.e_commerce.products.handlers;


import com.e_commerce.products.exceptions.BadRequestException;
import com.e_commerce.products.exceptions.details.BadRequestExceptionDetails;
import com.e_commerce.products.exceptions.details.IllegalAccessExceptionDetails;
import com.e_commerce.products.exceptions.details.MethodArgumentNotValidExceptionDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre) {
        return new ResponseEntity<>(
                BadRequestExceptionDetails
                        .builder()
                        .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .details(bre.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<IllegalAccessExceptionDetails> handlerIllegalAccessException(IllegalAccessException bre) {
        return new ResponseEntity<>(
                IllegalAccessExceptionDetails
                        .builder()
                        .title(HttpStatus.FORBIDDEN.getReasonPhrase())
                        .details(bre.getMessage())
                        .status(HttpStatus.FORBIDDEN.value())
                        .timestamp(LocalDateTime.now())
                        .build(), HttpStatus.FORBIDDEN);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        MethodArgumentNotValidExceptionDetails errorDetails = MethodArgumentNotValidExceptionDetails.builder()
                .title("Validation Failed")
                .details("One or more fields are invalid.")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
