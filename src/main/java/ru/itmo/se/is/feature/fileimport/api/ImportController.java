package ru.itmo.se.is.feature.fileimport.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import ru.itmo.se.is.feature.fileimport.api.dto.FileRequestDto;
import ru.itmo.se.is.feature.fileimport.application.ImportService;
import ru.itmo.se.is.platform.web.util.MultipartImportFileUtil;

@Path("/imports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class ImportController {

    @Inject
    private MultipartImportFileUtil multipartImportFileUtil;
    @Inject
    private ImportService importService;

    @POST
    public Response importMovies(@Multipart(value = "file") Attachment fileAttachment) throws Exception {
        FileRequestDto requestDto = multipartImportFileUtil.from(fileAttachment);
        importService.importMovies(requestDto);
        return Response.noContent().build();
    }
}
