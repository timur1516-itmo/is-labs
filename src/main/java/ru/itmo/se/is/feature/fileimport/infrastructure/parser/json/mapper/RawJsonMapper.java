package ru.itmo.se.is.feature.fileimport.infrastructure.parser.json.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.json.raw.RawJsonCoordinates;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.json.raw.RawJsonLocation;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.json.raw.RawJsonMovie;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.json.raw.RawJsonPerson;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;
import ru.itmo.se.is.feature.movie.api.dto.coordinates.CoordinatesRequestDto;
import ru.itmo.se.is.feature.person.api.dto.PersonRequestDto;
import ru.itmo.se.is.feature.person.api.dto.location.LocationRequestDto;
import ru.itmo.se.is.platform.mapper.MapperConfig;
import ru.itmo.se.is.shared.dto.common.EmbeddedObjectDto;

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
