package ru.itmo.se.is.feature.person.api.dto.location;

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
public class LocationRequestDto implements Serializable {
    @NotNull
    private Float x;

    @NotNull
    private Double y;

    @NotNull
    private Double z;
}
