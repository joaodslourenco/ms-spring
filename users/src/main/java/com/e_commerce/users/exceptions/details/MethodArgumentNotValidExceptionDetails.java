package com.e_commerce.users.exceptions.details;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@SuperBuilder
public class MethodArgumentNotValidExceptionDetails extends ExceptionDetails {
    Map<String, String> errors;
}
