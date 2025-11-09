package ru.itmo.se.is.platform.web.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;
import ru.itmo.se.is.shared.exception.EntityAlreadyExistsException;

@Provider
public class BusinessUniqueConstraintExceptionMapper implements ExceptionMapper<EntityAlreadyExistsException> {
    @Override
    public Response toResponse(EntityAlreadyExistsException exception) {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Unique constraint violation")
                .detail(exception.getMessage())
                .status(Response.Status.CONFLICT.getStatusCode())
                .code(ErrorCode.UNIQUE_CONSTRAINT_VIOLATION.name())
                .build();
        return Response.status(Response.Status.CONFLICT).entity(problemDetail).build();
    }
}
