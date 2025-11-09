package ru.itmo.se.is.feature.fileimport.infrastructure.parser.json.raw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.person.domain.value.Color;
import ru.itmo.se.is.feature.person.domain.value.Country;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawJsonPerson implements Serializable {
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private RawJsonLocation location;
    private Float weight;
    private Country nationality;
}
