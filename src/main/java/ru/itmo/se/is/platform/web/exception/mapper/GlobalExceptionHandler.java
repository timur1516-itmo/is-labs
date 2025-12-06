package ru.itmo.se.is.platform.web.exception.mapper;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;

@Slf4j
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException wae) {
            log.debug("Web application exception: {}", wae.getMessage());
            return wae.getResponse();
        }

        log.warn("Internal server error: {}", exception.getMessage(), exception);

        ProblemDetail problemDetail = ProblemDetail.builder()
                .title("Unknown error")
                .detail(exception.getMessage())
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .build();

        return Response
                .status(problemDetail.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(problemDetail)
                .build();
    }
}

