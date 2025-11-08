package ru.itmo.se.is.dto.importing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequestDto implements Serializable {
    private InputStream fileStream;
    private String fileName;
    private ImportFileFormat format;
}
