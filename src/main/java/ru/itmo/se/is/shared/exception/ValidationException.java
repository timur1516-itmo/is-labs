package ru.itmo.se.is.shared.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;

import java.util.List;
import java.util.Set;

@Getter
public class ValidationException extends BusinessException {

    private final List<FieldError> errors;

    public ValidationException(Set<? extends ConstraintViolation<?>> violations) {
        super(
                Response.Status.BAD_REQUEST,
                "Validation failed",
                ErrorCode.VALIDATION_ERROR,
                "Validation failed for " + violations.size() + " field(s)"
        );
        this.errors = violations.stream()
                .map(v -> new FieldError(
                        v.getPropertyPath().toString(),
                        v.getMessage()
                ))
                .toList();
    }

    @Override
    protected void enrichProblemDetail(ProblemDetail problemDetail) {
        problemDetail.setProperty("errors", errors);
    }

    public record FieldError(
            String field,
            String message
    ) {
    }
}
