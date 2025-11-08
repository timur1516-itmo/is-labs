package ru.itmo.se.is.mapper;

import org.mapstruct.Mapper;
import ru.itmo.se.is.config.MapperConfig;
import ru.itmo.se.is.dto.importing.ImportOperationResponseDto;
import ru.itmo.se.is.entity.ImportOperation;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface ImportOperationMapper {
    ImportOperationResponseDto toDto(ImportOperation importOperation);

    List<ImportOperationResponseDto> toDto(List<ImportOperation> importOperations);
}
