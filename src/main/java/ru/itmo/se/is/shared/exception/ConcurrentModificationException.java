package ru.itmo.se.is.shared.exception;

import jakarta.ws.rs.core.Response;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;

public class ConcurrentModificationException extends BusinessException {
    public ConcurrentModificationException(String message, Throwable cause) {
        super(
                Response.Status.CONFLICT,
                "Concurrent modification",
                ErrorCode.CONCURRENT_MODIFICATION,
                message,
                cause
        );
    }
}
