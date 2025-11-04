package ru.itmo.se.is.mapper;

import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.itmo.se.is.config.MapperConfig;
import ru.itmo.se.is.dto.movie.MovieRequestDto;
import ru.itmo.se.is.dto.movie.MovieResponseDto;
import ru.itmo.se.is.entity.Movie;
import ru.itmo.se.is.entity.Person;
import ru.itmo.se.is.service.PersonService;

import java.util.List;

@Mapper(config = MapperConfig.class)
public abstract class MovieMapper {

    @Inject
    private PersonService personService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "director", source = "directorId")
    @Mapping(target = "screenwriter", source = "screenwriterId")
    @Mapping(target = "operator", source = "operatorId")
    public abstract Movie toMovie(MovieRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "director", source = "directorId")
    @Mapping(target = "screenwriter", source = "screenwriterId")
    @Mapping(target = "operator", source = "operatorId")
    public abstract void toMovie(MovieRequestDto dto, @MappingTarget Movie movie);

    public abstract MovieResponseDto toDto(Movie movie);

    public abstract List<MovieResponseDto> toDto(List<Movie> movies);

    public Person map(Long personId) {
        if (personId == null) {
            return null;
        }
        return personService.getById(personId);
    }
}
