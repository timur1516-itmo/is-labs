package ru.itmo.se.is.platform.web.exception.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import ru.itmo.se.is.shared.dto.exception.ProblemDetail;
import ru.itmo.se.is.shared.exception.BusinessException;

@Slf4j
@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException exception) {
        ProblemDetail problemDetail = exception.toProblemDetail();

        log.debug("Business error: {}", exception.getMessage());

        return Response
                .status(exception.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(problemDetail)
                .build();
    }
}
