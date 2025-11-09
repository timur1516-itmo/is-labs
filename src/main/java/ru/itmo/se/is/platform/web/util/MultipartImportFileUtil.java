package ru.itmo.se.is.platform.web.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MultivaluedMap;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import ru.itmo.se.is.feature.fileimport.api.dto.FileUploadRequestDto;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportFileFormat;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MultipartImportFileUtil {

    private static InputStream getInputStream(InputPart filePart) {
        InputStream fileStream;
        try {
            fileStream = filePart.getBody(InputStream.class, null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not extract file input stream", e);
        }
        return fileStream;
    }

    public FileUploadRequestDto from(MultipartFormDataInput input) {
        InputPart filePart = getInputPart(input);
        String fileName = getFileName(filePart);
        ImportFileFormat format = getFormat(fileName);
        InputStream fileStream = getInputStream(filePart);

        FileUploadRequestDto dto = new FileUploadRequestDto();
        dto.setFileName(fileName);
        dto.setFileStream(fileStream);
        dto.setFormat(format);

        return dto;
    }

    private InputPart getInputPart(MultipartFormDataInput input) {
        Map<String, List<InputPart>> formParts = input.getFormDataMap();

        List<InputPart> fileParts = formParts.get("file");
        if (fileParts == null || fileParts.isEmpty()) {
            throw new IllegalArgumentException("File was not provided (expected form-data param 'file')");
        }
        if (fileParts.size() > 1) {
            throw new IllegalArgumentException(String.format("Expected only one file, got: %d", fileParts.size()));
        }
        return fileParts.get(0);
    }

    private ImportFileFormat getFormat(@NotNull String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException(
                    String.format("File does not have extension: %s", fileName)
            );
        }

        String ext = fileName.substring(lastDotIndex + 1).toUpperCase();

        try {
            return ImportFileFormat.valueOf(ext);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    String.format("Unknown file format: %s (extension: %s)", fileName, ext)
            );
        }
    }

    private String getFileName(InputPart part) {
        MultivaluedMap<String, String> headers = part.getHeaders();
        String contentDisposition = headers.getFirst("Content-Disposition");

        if (contentDisposition == null) {
            throw new IllegalArgumentException("Missing 'Content-Disposition' header");
        }

        String fileNameParam = Arrays.stream(contentDisposition.split(";"))
                .map(String::trim)
                .filter(s -> s.contains("filename="))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing 'filename' param"));

        return fileNameParam
                .substring("filename=".length())
                .trim()
                .replace("\"", "");
    }
}
