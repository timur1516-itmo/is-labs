package ru.itmo.se.is.shared.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;

import java.util.List;

@Getter
public class ImportDataValidationException extends BusinessException {

    private final List<ValidationError> errors;

    public ImportDataValidationException(List<ValidationError> errors) {
        super(
                Response.Status.BAD_REQUEST,
                "Import data validation failed",
                ErrorCode.IMPORT_DATA_VALIDATION,
                "Validation failed for " + errors.size() + " field(s)"
        );
        this.errors = List.copyOf(errors);
    }

    @Override
    protected void enrichProblemDetail(ProblemDetail problemDetail) {
        problemDetail.setProperty("errors", errors);
    }

    public record ValidationError(
            long index,
            String objectName,
            String propertyPath,
            String message,
            Object invalidValue
    ) {
    }
}
