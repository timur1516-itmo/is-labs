package ru.itmo.se.is.feature.fileimport.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportOperationStatus;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportOperationResponseDto implements Serializable {
    private Long id;
    private ImportOperationStatus status;
    private ZonedDateTime startDt;
    private ZonedDateTime endDt;
    private Long importedCnt;
    private String errorMessage;
}
