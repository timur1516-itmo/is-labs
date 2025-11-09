package ru.itmo.se.is.feature.movie.api.dto.coordinates;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesRequestDto implements Serializable {
    @NotNull
    @DecimalMin(value = "-738", inclusive = false)
    private Double x;

    @NotNull
    @DecimalMax(value = "462")
    private Long y;
}
