package ru.itmo.se.is.feature.fileimport.api;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.itmo.se.is.feature.fileimport.api.dto.FileResponseDto;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationPagingAndSortingBeanParamDto;
import ru.itmo.se.is.feature.fileimport.application.ImportOperationService;

import java.io.ByteArrayInputStream;

@Path("/imports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImportOperationController {

    @Inject
    private ImportOperationService importOperationService;

    @GET
    public Response getAllImportOperations(@Valid @BeanParam ImportOperationPagingAndSortingBeanParamDto lazyBeanParamDto) {
        return Response.ok(importOperationService.getPagingAndSorting(lazyBeanParamDto)).build();
    }

    @GET
    @Path("/{id}/file")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadImportFile(@PathParam("id") long operationId) throws Exception {
        FileResponseDto dto = importOperationService.getFile(operationId);
        return Response.ok(new ByteArrayInputStream(dto.getContent()))
                .type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + dto.getFileName() + "\"")
                .build();
    }
}
