package ru.itmo.se.is.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.itmo.se.is.entity.value.Color;
import ru.itmo.se.is.entity.value.Country;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Person {
    private Long id;
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private Location location;
    private float weight;
    private Country nationality;
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }
}
