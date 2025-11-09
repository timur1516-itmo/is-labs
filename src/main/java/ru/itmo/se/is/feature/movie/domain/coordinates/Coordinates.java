package ru.itmo.se.is.feature.movie.domain.coordinates;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Coordinates {
    private Double x;
    private long y;
}
