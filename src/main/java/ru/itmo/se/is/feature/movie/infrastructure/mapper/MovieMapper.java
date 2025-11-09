package ru.itmo.se.is.feature.movie.infrastructure.mapper;

import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;
import ru.itmo.se.is.feature.movie.api.dto.MovieResponseDto;
import ru.itmo.se.is.feature.movie.domain.Movie;
import ru.itmo.se.is.feature.person.api.dto.PersonRequestDto;
import ru.itmo.se.is.feature.person.application.PersonService;
import ru.itmo.se.is.feature.person.domain.Person;
import ru.itmo.se.is.feature.person.infrastructure.mapper.PersonMapper;
import ru.itmo.se.is.platform.mapper.MapperConfig;
import ru.itmo.se.is.shared.dto.common.EmbeddedObjectDto;

import java.util.List;

@Mapper(config = MapperConfig.class)
public abstract class MovieMapper {

    @Inject
    private PersonService personService;

    @Inject
    private PersonMapper personMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "director", source = "directorReference")
    @Mapping(target = "screenwriter", source = "screenwriterReference")
    @Mapping(target = "operator", source = "operatorReference")
    public abstract Movie toMovie(MovieRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "director", source = "directorReference")
    @Mapping(target = "screenwriter", source = "screenwriterReference")
    @Mapping(target = "operator", source = "operatorReference")
    public abstract void toMovie(MovieRequestDto dto, @MappingTarget Movie movie);

    public abstract MovieResponseDto toDto(Movie movie);

    public abstract List<MovieResponseDto> toDto(List<Movie> movies);

    public Person map(EmbeddedObjectDto<Long, PersonRequestDto> personReference) {
        if (personReference == null || personReference.isEmpty()) {
            return null;
        }
        if (personReference.isReference()) {
            return personService.getById(personReference.getId());
        }
        return personMapper.toPerson(personReference.getValue());
    }
}
