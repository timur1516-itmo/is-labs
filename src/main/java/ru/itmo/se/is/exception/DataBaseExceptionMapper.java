package ru.itmo.se.is.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

@Provider
public class DataBaseExceptionMapper implements ExceptionMapper<DatabaseException> {
    @Override
    public Response toResponse(DatabaseException exception) {
        Throwable internal = exception.getInternalException();
        if (internal instanceof SQLException sqlEx) {
            if ("23503".equals(sqlEx.getSQLState())) {
                return processDeletionConflictException();
            }
        }
        throw exception;
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
}
