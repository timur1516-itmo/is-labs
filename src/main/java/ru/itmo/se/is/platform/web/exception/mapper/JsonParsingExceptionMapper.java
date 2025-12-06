package ru.itmo.se.is.platform.web.exception.mapper;

import jakarta.json.stream.JsonParsingException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;
import ru.itmo.se.is.shared.exception.InvalidJsonException;

@Provider
public class JsonParsingExceptionMapper implements ExceptionMapper<JsonParsingException> {

    @Override
    public Response toResponse(JsonParsingException exception) {
        InvalidJsonException be = new InvalidJsonException(
                exception.getMessage() != null ? exception.getMessage() : "Invalid JSON",
                exception
        );
        ProblemDetail problemDetail = be.toProblemDetail();

        return Response
                .status(be.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(problemDetail)
                .build();
    }
}
