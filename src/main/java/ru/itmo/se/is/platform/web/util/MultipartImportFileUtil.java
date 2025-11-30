package ru.itmo.se.is.platform.web.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import ru.itmo.se.is.feature.fileimport.api.dto.FileUploadRequestDto;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportFileFormat;

import java.io.InputStream;

@ApplicationScoped
public class MultipartImportFileUtil {

    public FileUploadRequestDto from(Attachment attachment) {
        String fileName = getFileName(attachment);
        ImportFileFormat format = getFormat(fileName);
        InputStream fileStream = getInputStream(attachment);

        FileUploadRequestDto dto = new FileUploadRequestDto();
        dto.setFileName(fileName);
        dto.setFileStream(fileStream);
        dto.setFormat(format);

        return dto;
    }

    private InputStream getInputStream(Attachment attachment) {
        InputStream fileStream;
        try {
            fileStream = attachment.getDataHandler().getInputStream();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not extract file input stream", e);
        }
        return fileStream;
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

    private String getFileName(Attachment attachment) {
        String fileName = attachment.getDataHandler().getName();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name is not provided");
        }
        return fileName.trim();
    }
}
