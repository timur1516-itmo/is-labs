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
public class RawJsonLocation implements Serializable {
    private Float x;
    private Double y;
    private Double z;
}
