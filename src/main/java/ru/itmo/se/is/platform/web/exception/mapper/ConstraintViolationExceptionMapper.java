package ru.itmo.se.is.platform.web.exception.mapper;


import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;
import ru.itmo.se.is.shared.exception.ValidationException;

@Slf4j
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ValidationException be = new ValidationException(exception.getConstraintViolations());
        ProblemDetail problemDetail = be.toProblemDetail();

        log.debug("Constraint violation exception: {}", exception.getMessage());

        return Response
                .status(be.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(problemDetail)
                .build();
    }
}
