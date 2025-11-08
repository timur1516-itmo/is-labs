package ru.itmo.se.is.exception;

public class BusinessUniqueConstraintException extends RuntimeException {
    public BusinessUniqueConstraintException(String message) {
        super(message);
    }
}
