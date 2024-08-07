package com.e_commerce.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class IllegalAccessException extends RuntimeException {
    public IllegalAccessException(String message) {
        super(message);
    }
}
