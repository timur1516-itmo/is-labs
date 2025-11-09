package ru.itmo.se.is.feature.person.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.person.api.dto.location.LocationResponseDto;
import ru.itmo.se.is.feature.person.domain.value.Color;
import ru.itmo.se.is.feature.person.domain.value.Country;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponseDto implements Serializable {
    private Long id;
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private LocationResponseDto location;
    private Float weight;
    private Country nationality;
}
