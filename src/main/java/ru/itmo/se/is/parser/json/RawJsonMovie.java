package ru.itmo.se.is.parser.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.entity.value.MovieGenre;
import ru.itmo.se.is.entity.value.MpaaRating;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawJsonMovie {
    private String name;
    private RawJsonCoordinates coordinates;
    private Integer oscarsCount;
    private Float budget;
    private Integer totalBoxOffice;
    private MpaaRating mpaaRating;
    private RawJsonPerson director;
    private RawJsonPerson screenwriter;
    private RawJsonPerson operator;
    private Integer length;
    private Integer goldenPalmCount;
    private Integer usaBoxOffice;
    private String tagline;
    private MovieGenre genre;
}
