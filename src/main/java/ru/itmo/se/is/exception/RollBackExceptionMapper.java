package ru.itmo.se.is.exception;

import jakarta.transaction.RollbackException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RollBackExceptionMapper implements ExceptionMapper<RollbackException> {
    @Override
    public Response toResponse(RollbackException exception) {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Modification conflict")
                .detail("Entity has already been modified by another user")
                .status(Response.Status.CONFLICT.getStatusCode())
                .code(ErrorCode.CONCURRENT_MODIFICATION.name())
                .build();
        return Response.status(Response.Status.CONFLICT).entity(problemDetail).build();
    }
}
