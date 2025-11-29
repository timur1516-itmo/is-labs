package ru.itmo.se.is.feature.fileimport.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationPagingAndSortingBeanParamDto;
import ru.itmo.se.is.feature.fileimport.application.ImportOperationService;

@Path("/imports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImportOperationController {

    @Inject
    private ImportOperationService importOperationService;

    @GET
    public Response getAllImportOperations(@BeanParam ImportOperationPagingAndSortingBeanParamDto lazyBeanParamDto) {
        return Response.ok(importOperationService.getPagingAndSorting(lazyBeanParamDto)).build();
    }
}
