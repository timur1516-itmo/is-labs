package ru.itmo.se.is.platform.web.exception.mapper;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException wae) {
            return wae.getResponse();
        }

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

