package ru.itmo.se.is.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.entity.value.MovieGenre;
import ru.itmo.se.is.entity.value.MpaaRating;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
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
