package ru.itmo.se.is.feature.person.domain.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Location {
    private Float x;
    private Double y;
    private double z;
}
