package ru.itmo.se.is.feature.movie.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;
import ru.itmo.se.is.feature.movie.api.dto.MovieResponseDto;
import ru.itmo.se.is.feature.movie.domain.Movie;
import ru.itmo.se.is.platform.mapper.MapperConfig;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface MovieMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "director", ignore = true)
    @Mapping(target = "screenwriter", ignore = true)
    @Mapping(target = "operator", ignore = true)
    Movie toMovie(MovieRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "director", ignore = true)
    @Mapping(target = "screenwriter", ignore = true)
    @Mapping(target = "operator", ignore = true)
    void toMovie(MovieRequestDto dto, @MappingTarget Movie movie);

    MovieResponseDto toDto(Movie movie);

    List<MovieResponseDto> toDto(List<Movie> movies);
}
