package com.syrtin.banking.controller;

import com.syrtin.banking.validation.ValidationError;
import com.syrtin.banking.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionHandlingController {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<List<ValidationError>> handleValidationException(ValidationException ex) {
        List<ValidationError> errors = ex.getErrors();
        return ResponseEntity.badRequest().body(errors);
    }
}
