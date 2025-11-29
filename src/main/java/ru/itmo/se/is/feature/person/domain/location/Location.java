package ru.itmo.se.is.feature.person.domain.location;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Embeddable
public class Location {

    @Column(nullable = false)
    private Float x;

    @Column(nullable = false)
    private Double y;

    @Column(nullable = false)
    private double z;
}
