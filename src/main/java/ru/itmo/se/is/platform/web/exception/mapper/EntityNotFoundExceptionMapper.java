package ru.itmo.se.is.platform.web.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;
import ru.itmo.se.is.shared.exception.EntityNotFoundException;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {
    @Override
    public Response toResponse(EntityNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Entity not found")
                .detail(exception.getMessage())
                .status(Response.Status.NOT_FOUND.getStatusCode())
                .code(ErrorCode.NOT_FOUND.name())
                .build();
        return Response.status(Response.Status.NOT_FOUND).entity(problemDetail).build();
    }
}
