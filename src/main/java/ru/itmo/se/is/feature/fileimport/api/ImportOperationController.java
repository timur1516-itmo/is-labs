package ru.itmo.se.is.feature.fileimport.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationPagingAndSortingBeanParamDto;
import ru.itmo.se.is.feature.fileimport.application.ImportOperationService;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.shared.storage.FileStorage;

import java.io.InputStream;

@Path("/imports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImportOperationController {

    @Inject
    private ImportOperationService importOperationService;
    @Inject
    private FileStorage fileStorage;

    @GET
    public Response getAllImportOperations(@BeanParam ImportOperationPagingAndSortingBeanParamDto lazyBeanParamDto) {
        return Response.ok(importOperationService.getPagingAndSorting(lazyBeanParamDto)).build();
    }

    @GET
    @Path("/{id}/file")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadImportFile(@PathParam("id") long operationId) {
        ImportOperation operation = importOperationService.getById(operationId);
        if (operation == null) {
            throw new NotFoundException("Import operation not found: " + operationId);
        }

        InputStream fileStream = fileStorage.get(
                operation.getFileBucket(),
                operation.getFileObjectKey()
        );

        String fileName = operation.getFileName();

        return Response.ok(fileStream)
                .type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .build();
    }
}
