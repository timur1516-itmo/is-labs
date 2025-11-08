package ru.itmo.se.is.parser.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawJsonCoordinates implements Serializable {
    private Double x;
    private Long y;
}
