package ru.itmo.se.is.feature.person.infrastructure.mapper.location;

import org.mapstruct.Mapper;
import ru.itmo.se.is.feature.person.api.dto.location.LocationRequestDto;
import ru.itmo.se.is.feature.person.api.dto.location.LocationResponseDto;
import ru.itmo.se.is.feature.person.domain.location.Location;
import ru.itmo.se.is.platform.mapper.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface LocationMapper {
    Location toLocation(LocationRequestDto dto);

    LocationResponseDto toDto(Location location);
}
