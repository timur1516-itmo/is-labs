package ru.itmo.se.is.feature.movie.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.movie.api.dto.coordinates.CoordinatesResponseDto;
import ru.itmo.se.is.feature.movie.domain.value.MovieGenre;
import ru.itmo.se.is.feature.movie.domain.value.MpaaRating;
import ru.itmo.se.is.feature.person.api.dto.PersonResponseDto;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponseDto implements Serializable {
    private Long id;
    private String name;
    private CoordinatesResponseDto coordinates;
    private ZonedDateTime creationDate;
    private Integer oscarsCount;
    private Float budget;
    private Integer totalBoxOffice;
    private MpaaRating mpaaRating;
    private PersonResponseDto director;
    private PersonResponseDto screenwriter;
    private PersonResponseDto operator;
    private Integer length;
    private Integer goldenPalmCount;
    private Integer usaBoxOffice;
    private String tagline;
    private MovieGenre genre;
}
