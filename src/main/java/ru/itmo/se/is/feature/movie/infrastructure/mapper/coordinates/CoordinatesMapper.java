package ru.itmo.se.is.feature.movie.infrastructure.mapper.coordinates;

import org.mapstruct.Mapper;
import ru.itmo.se.is.feature.movie.api.dto.coordinates.CoordinatesRequestDto;
import ru.itmo.se.is.feature.movie.api.dto.coordinates.CoordinatesResponseDto;
import ru.itmo.se.is.feature.movie.domain.coordinates.Coordinates;
import ru.itmo.se.is.platform.mapper.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface CoordinatesMapper {
    Coordinates toCoordinates(CoordinatesRequestDto dto);

    CoordinatesResponseDto toDto(Coordinates coordinates);
}
