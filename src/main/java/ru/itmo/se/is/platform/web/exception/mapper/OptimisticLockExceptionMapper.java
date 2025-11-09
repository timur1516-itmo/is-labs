package ru.itmo.se.is.platform.web.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.persistence.exceptions.OptimisticLockException;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;

@Provider
public class OptimisticLockExceptionMapper implements ExceptionMapper<OptimisticLockException> {
    @Override
    public Response toResponse(OptimisticLockException exception) {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Modification conflict")
                .detail("Entity has already been modified by another user")
                .status(Response.Status.CONFLICT.getStatusCode())
                .code(ErrorCode.CONCURRENT_MODIFICATION.name())
                .build();
        return Response.status(Response.Status.CONFLICT).entity(problemDetail).build();
    }
}
