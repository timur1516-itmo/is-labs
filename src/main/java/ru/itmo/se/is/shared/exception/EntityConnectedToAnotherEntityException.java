package ru.itmo.se.is.shared.exception;

import jakarta.ws.rs.core.Response;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;

public class EntityConnectedToAnotherEntityException extends BusinessException {
    public EntityConnectedToAnotherEntityException(String message, Throwable cause) {
        super(
                Response.Status.CONFLICT,
                "Entity connected to another entity",
                ErrorCode.ENTITY_CONNECTED,
                message,
                cause
        );
    }
}
