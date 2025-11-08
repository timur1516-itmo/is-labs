package ru.itmo.se.is.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import ru.itmo.se.is.controller.util.MultipartImportFileUtil;
import ru.itmo.se.is.dto.importing.FileUploadRequestDto;
import ru.itmo.se.is.dto.importing.ImportOperationLazyBeanParamDto;
import ru.itmo.se.is.dto.importing.ImportOperationResponseDto;
import ru.itmo.se.is.dto.person.PersonLazyBeanParamDto;
import ru.itmo.se.is.service.ImportOperationService;
import ru.itmo.se.is.service.ImportService;

@Path("/imports")
@Produces(MediaType.APPLICATION_JSON)
public class ImportController {

    @Inject
    private MultipartImportFileUtil multipartImportFileUtil;
    @Inject
    private ImportService importService;
    @Inject
    private ImportOperationService importOperationService;

    @GET
    public Response getAllImportOperations(@BeanParam ImportOperationLazyBeanParamDto lazyBeanParamDto) {
        return Response.ok(importOperationService.lazyGet(lazyBeanParamDto)).build();
    }


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importMovies(MultipartFormDataInput input) {
        FileUploadRequestDto requestDto = multipartImportFileUtil.from(input);
        ImportOperationResponseDto responseDto = importService.importMovies(requestDto);
        return Response.ok(responseDto).build();
    }
}
