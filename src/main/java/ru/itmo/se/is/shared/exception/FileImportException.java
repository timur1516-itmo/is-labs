package ru.itmo.se.is.shared.exception;

import jakarta.ws.rs.core.Response;
import ru.itmo.se.is.shared.dto.exception.ErrorCode;

public class FileImportException extends BusinessException {
    public FileImportException(String message) {
        super(
                Response.Status.BAD_REQUEST,
                "File import error",
                ErrorCode.FILE_IMPORT_ERROR,
                message
        );
    }

    public FileImportException(String message, Throwable cause) {
        super(
                Response.Status.BAD_REQUEST,
                "File import error",
                ErrorCode.FILE_IMPORT_ERROR,
                message,
                cause
        );
    }
}
