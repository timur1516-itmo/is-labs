package ru.itmo.se.is.feature.movie.api.dto.coordinates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesResponseDto implements Serializable {
    private Double x;
    private Long y;
}
