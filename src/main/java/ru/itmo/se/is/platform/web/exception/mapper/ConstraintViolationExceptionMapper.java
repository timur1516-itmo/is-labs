package ru.itmo.se.is.platform.web.exception.mapper;


import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;

import java.util.List;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Validation failed")
                .detail("One or more validation errors occurred.")
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .code(ErrorCode.VALIDATION_ERROR.name())
                .build();

        List<Violation> errors = exception.getConstraintViolations()
                .stream()
                .map(cv -> new Violation(cv.getPropertyPath().toString(), cv.getMessage()))
                .toList();

        problemDetail.setProperty("errors", errors);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(problemDetail)
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Violation {
        private String field;
        private String message;
    }
}
