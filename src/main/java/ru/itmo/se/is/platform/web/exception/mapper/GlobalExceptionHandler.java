package ru.itmo.se.is.platform.web.exception.mapper;

import jakarta.json.bind.JsonbException;
import jakarta.json.stream.JsonParsingException;
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;

import java.sql.SQLException;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException wae)
            return wae.getResponse();

        ProblemDetail problemDetail = getProblemDetail(exception);

        return Response
                .status(problemDetail.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(problemDetail).build();
    }

    private ProblemDetail getProblemDetail(Throwable exception) {
        Throwable cause = exception;
        while (cause != null) {
            if (cause instanceof JsonbException || cause instanceof JsonParsingException)
                return handleJsonParsingException(cause);
            if (cause instanceof SQLException sqlEx && "23503".equals(sqlEx.getSQLState()))
                return processDeletionConflictException();
            if (cause instanceof SQLException sqlEx && "40001".equals(sqlEx.getSQLState()) || cause instanceof OptimisticLockException)
                return processConcurrentModificationException();
            cause = cause.getCause();
        }
        assert exception != null;
        return handleException(exception);
    }

    private ProblemDetail handleException(Throwable exception) {
        return ProblemDetail.builder()
                .title("Unknown error")
                .detail(exception.getMessage())
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .code(ErrorCode.INTERNAL_SERVER_ERROR.name())
                .build();
    }

    private ProblemDetail handleJsonParsingException(Throwable exception) {
        return ProblemDetail.builder()
                .title("Invalid JSON")
                .detail(exception.getMessage())
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .code(ErrorCode.INVALID_JSON.name())
                .build();
    }

    private ProblemDetail processDeletionConflictException() {
        return ProblemDetail.builder()
                .title("Could not delete entity")
                .detail("Entity is connected to another entity")
                .status(Response.Status.CONFLICT.getStatusCode())
                .code(ErrorCode.DELETION_CONFLICT.name())
                .build();
    }

    private ProblemDetail processConcurrentModificationException() {
        return ProblemDetail.builder()
                .title("Modification conflict")
                .detail("Entity has already been modified by another user")
                .status(Response.Status.CONFLICT.getStatusCode())
                .code(ErrorCode.CONCURRENT_MODIFICATION.name())
                .build();
    }
}
