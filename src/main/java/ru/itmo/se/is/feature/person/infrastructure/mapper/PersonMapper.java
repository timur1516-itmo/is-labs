package ru.itmo.se.is.feature.person.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.itmo.se.is.feature.person.api.dto.PersonRequestDto;
import ru.itmo.se.is.feature.person.api.dto.PersonResponseDto;
import ru.itmo.se.is.feature.person.domain.Person;
import ru.itmo.se.is.platform.mapper.MapperConfig;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface PersonMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    Person toPerson(PersonRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    void toPerson(PersonRequestDto dto, @MappingTarget Person entity);

    PersonResponseDto toDto(Person person);

    List<PersonResponseDto> toDto(List<Person> persons);
}
