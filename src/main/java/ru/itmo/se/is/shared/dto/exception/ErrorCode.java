package ru.itmo.se.is.shared.dto.exception;

public enum ErrorCode {
    VALIDATION_ERROR,
    INVALID_JSON,
    NOT_FOUND,
    DELETION_CONFLICT,
    CONCURRENT_MODIFICATION,
    UNIQUE_CONSTRAINT_VIOLATION,
    INTERNAL_SERVER_ERROR
}
