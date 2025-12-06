package ru.itmo.se.is.shared.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final Response.Status status;
    private final String title;
    private final ErrorCode code;

    protected BusinessException(Response.Status status,
                                String title,
                                ErrorCode code,
                                String message) {
        super(message);
        this.status = status;
        this.title = title;
        this.code = code;
    }

    protected BusinessException(Response.Status status,
                                String title,
                                ErrorCode code,
                                String message,
                                Throwable cause) {
        super(message, cause);
        this.status = status;
        this.title = title;
        this.code = code;
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.builder()
                .title(title)
                .detail(getMessage())
                .status(status.getStatusCode())
                .code(code)
                .build();
        enrichProblemDetail(problemDetail);
        return problemDetail;
    }

    protected void enrichProblemDetail(ProblemDetail problemDetail) {
    }
}
