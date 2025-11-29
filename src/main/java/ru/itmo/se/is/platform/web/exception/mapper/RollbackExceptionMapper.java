package ru.itmo.se.is.platform.web.exception.mapper;

import jakarta.transaction.RollbackException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;

import java.sql.SQLException;

@Provider
public class RollbackExceptionMapper implements ExceptionMapper<RollbackException> {

    @Override
    public Response toResponse(RollbackException exception) {
        if(exception.getMessage().equals("ARJUNA016053: Could not commit transaction.")) {
            return processConcurrentModificationException();
        }
        Throwable cause = exception;
        while (cause != null) {
            if (cause instanceof SQLException sqlEx) {
                if ("23503".equals(sqlEx.getSQLState())) {
                    return processDeletionConflictException();
                }
                if ("40001".equals(sqlEx.getSQLState())) {
                    return processConcurrentModificationException();
                }
            }
            cause = cause.getCause();
        }
        throw new RuntimeException(exception);
    }

    private Response processDeletionConflictException() {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Could not delete entity")
                .detail("Entity is connected to another entity")
                .status(Response.Status.CONFLICT.getStatusCode())
                .code(ErrorCode.DELETION_CONFLICT.name())
                .build();
        return Response.status(Response.Status.CONFLICT).entity(problemDetail).build();
    }

    private Response processConcurrentModificationException() {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Modification conflict")
                .detail("Entity has already been modified by another user")
                .status(Response.Status.CONFLICT.getStatusCode())
                .code(ErrorCode.CONCURRENT_MODIFICATION.name())
                .build();
        return Response.status(Response.Status.CONFLICT).entity(problemDetail).build();
    }
}
