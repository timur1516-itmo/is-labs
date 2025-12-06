package ru.itmo.se.is.shared.exception;

import jakarta.ws.rs.core.Response;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;

public class InvalidJsonException extends BusinessException {
    public InvalidJsonException(String message, Throwable cause) {
        super(
                Response.Status.BAD_REQUEST,
                "Invalid JSON",
                ErrorCode.INVALID_JSON,
                message,
                cause
        );
    }
}
