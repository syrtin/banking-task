package com.syrtin.banking.validation;

import java.util.List;

public class ValidationException extends Exception {

    private final List<ValidationError> errors;

    public ValidationException(List<ValidationError> errors) {
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}