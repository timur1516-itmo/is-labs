package ru.itmo.se.is.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.dto.coordinates.CoordinatesResponseDto;
import ru.itmo.se.is.dto.person.PersonResponseDto;
import ru.itmo.se.is.entity.value.MovieGenre;
import ru.itmo.se.is.entity.value.MpaaRating;

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
