package ru.itmo.se.is.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itmo.se.is.config.MapperConfig;
import ru.itmo.se.is.dto.EmbeddedObjectDto;
import ru.itmo.se.is.dto.coordinates.CoordinatesRequestDto;
import ru.itmo.se.is.dto.location.LocationRequestDto;
import ru.itmo.se.is.dto.movie.MovieRequestDto;
import ru.itmo.se.is.dto.person.PersonRequestDto;
import ru.itmo.se.is.parser.json.RawJsonCoordinates;
import ru.itmo.se.is.parser.json.RawJsonLocation;
import ru.itmo.se.is.parser.json.RawJsonMovie;
import ru.itmo.se.is.parser.json.RawJsonPerson;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface RawJsonMapper {

    @Mapping(target = "directorReference", source = "director")
    @Mapping(target = "screenwriterReference", source = "screenwriter")
    @Mapping(target = "operatorReference", source = "operator")
    MovieRequestDto toMovieRequestDto(RawJsonMovie rawJsonMovie);

    List<MovieRequestDto> toMovieRequestDto(List<RawJsonMovie> rawJsonMovie);

    PersonRequestDto toPersonRequestDto(RawJsonPerson rawJsonPerson);

    LocationRequestDto toLocationRequestDto(RawJsonLocation rawJsonLocation);

    CoordinatesRequestDto toCoordinatesRequestDto(RawJsonCoordinates rawJsonCoordinates);

    default EmbeddedObjectDto<Long, PersonRequestDto> map(RawJsonPerson person) {
        if (person == null) {
            return new EmbeddedObjectDto<>(null, null);
        }
        EmbeddedObjectDto<Long, PersonRequestDto> result = new EmbeddedObjectDto<>();
        result.setId(null);
        result.setValue(toPersonRequestDto(person));
        return result;
    }
}
