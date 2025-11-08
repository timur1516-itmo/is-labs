package ru.itmo.se.is.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessUniqueConstraintExceptionMapper implements ExceptionMapper<BusinessUniqueConstraintException> {
    @Override
    public Response toResponse(BusinessUniqueConstraintException exception) {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Unique constraint violation")
                .detail(exception.getMessage())
                .status(Response.Status.CONFLICT.getStatusCode())
                .code(ErrorCode.UNIQUE_CONSTRAINT_VIOLATION.name())
                .build();
        return Response.status(Response.Status.CONFLICT).entity(problemDetail).build();
    }
}
