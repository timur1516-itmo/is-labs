package ru.itmo.se.is.platform.web.exception.mapper;

import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;
import ru.itmo.se.is.shared.exception.InvalidJsonException;

@Slf4j
@Provider
public class JsonbExceptionMapper implements ExceptionMapper<JsonbException> {

    @Override
    public Response toResponse(JsonbException exception) {
        InvalidJsonException be = new InvalidJsonException(
                exception.getMessage() != null ? exception.getMessage() : "Invalid JSON",
                exception
        );
        ProblemDetail problemDetail = be.toProblemDetail();

        log.debug("Json parse exception: {}", exception.getMessage());

        return Response
                .status(be.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(problemDetail)
                .build();
    }
}

