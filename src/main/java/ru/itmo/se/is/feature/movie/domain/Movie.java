package ru.itmo.se.is.feature.movie.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.itmo.se.is.feature.movie.domain.coordinates.Coordinates;
import ru.itmo.se.is.feature.movie.domain.value.MovieGenre;
import ru.itmo.se.is.feature.movie.domain.value.MpaaRating;
import ru.itmo.se.is.feature.person.domain.Person;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Movie {
    private Long id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private int oscarsCount;
    private float budget;
    private int totalBoxOffice;
    private MpaaRating mpaaRating;
    private Person director;
    private Person screenwriter;
    private Person operator;
    private Integer length;
    private Integer goldenPalmCount;
    private int usaBoxOffice;
    private String tagline;
    private MovieGenre genre;
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }
}
