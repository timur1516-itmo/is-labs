package ru.itmo.se.is.platform.web.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import ru.itmo.se.is.feature.fileimport.api.dto.FileRequestDto;
import ru.itmo.se.is.feature.fileimport.domain.value.FileExtension;
import ru.itmo.se.is.shared.exception.FileImportException;

@ApplicationScoped
public class MultipartImportFileUtil {

    public FileRequestDto from(Attachment attachment) {
        String fileName = getFileName(attachment);
        FileExtension format = getFormat(fileName);
        byte[] content = getContent(attachment);

        FileRequestDto dto = new FileRequestDto();
        dto.setFileName(fileName);
        dto.setContent(content);
        dto.setFileExtension(format);

        return dto;
    }

    private byte[] getContent(Attachment attachment) {
        try {
            return attachment
                    .getDataHandler()
                    .getInputStream()
                    .readAllBytes();
        } catch (Exception e) {
            throw new FileImportException("Could not extract file input stream", e);
        }
    }

    private FileExtension getFormat(@NotNull String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == fileName.length() - 1) {
            throw new FileImportException(
                    String.format("File does not have extension: %s", fileName)
            );
        }

        String ext = fileName.substring(lastDotIndex + 1).toUpperCase();

        try {
            return FileExtension.valueOf(ext);
        } catch (IllegalArgumentException ex) {
            throw new FileImportException(
                    String.format("Unknown file format: %s (extension: %s)", fileName, ext),
                    ex
            );
        }
    }

    private String getFileName(Attachment attachment) {
        String fileName = attachment.getDataHandler().getName();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new FileImportException("File name is not provided");
        }
        return fileName.trim();
    }
}
