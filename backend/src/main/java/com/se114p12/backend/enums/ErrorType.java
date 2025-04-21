package com.se114p12.backend.enums;

import lombok.Getter;

@Getter
public enum ErrorType {
    VALIDATION_ERROR("Validation error occurred"),
    MISSING_FIELD_ERROR("Required field is missing"),
    INVALID_INPUT_ERROR("Input provided is invalid"),
    AUTHENTICATION_ERROR("Authentication failed"),
    AUTHORIZATION_ERROR("Authorization failed"),
    RESOURCE_NOT_FOUND_ERROR("Resource not found"),
    RESOURCE_CONFLICT_ERROR("Resource conflict occurred"),
    RESOURCE_GONE_ERROR("Resource is no longer available"),
    SERVER_ERROR("Internal server error"),
    BAD_REQUEST_ERROR("Bad request");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

}
