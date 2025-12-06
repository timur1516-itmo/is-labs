package ru.itmo.se.is.feature.fileimport.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {
    private byte[] content;
    private String fileName;
}
