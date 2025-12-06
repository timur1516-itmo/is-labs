package ru.itmo.se.is.shared.exception;

import jakarta.ws.rs.core.Response;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;

public class EntityAlreadyExistsException extends BusinessException {
    public EntityAlreadyExistsException(String message) {
        super(
                Response.Status.CONFLICT,
                "Entity already exists",
                ErrorCode.ENTITY_ALREADY_EXISTS,
                message
        );
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(
                Response.Status.CONFLICT,
                "Entity already exists",
                ErrorCode.ENTITY_ALREADY_EXISTS,
                message,
                cause
        );
    }
}
