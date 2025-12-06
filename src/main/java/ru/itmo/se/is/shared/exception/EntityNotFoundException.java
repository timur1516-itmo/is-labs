package ru.itmo.se.is.shared.exception;

import jakarta.ws.rs.core.Response;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message) {
        super(
                Response.Status.NOT_FOUND,
                "Entity not found",
                ErrorCode.NOT_FOUND,
                message
        );
    }
}
