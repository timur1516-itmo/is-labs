package ru.itmo.se.is.feature.fileimport.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.fileimport.domain.value.FileExtension;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileRequestDto implements Serializable {
    private byte[] content;
    private String fileName;
    private FileExtension fileExtension;
}
