package com.syrtin.banking.validation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationError {
    @JsonProperty("Error")
    private final String errorMessage;

    public ValidationError(String invalidField, String description) {
        this.errorMessage = String.format("Validation fail for %s : %s", invalidField, description);
    }
}
