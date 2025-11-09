package ru.itmo.se.is.feature.fileimport.infrastructure.mapper;

import org.mapstruct.Mapper;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationResponseDto;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.platform.mapper.MapperConfig;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface ImportOperationMapper {
    ImportOperationResponseDto toDto(ImportOperation importOperation);

    List<ImportOperationResponseDto> toDto(List<ImportOperation> importOperations);
}
