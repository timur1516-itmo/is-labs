package ru.itmo.se.is.repository;

import ru.itmo.se.is.entity.Person;

import java.util.Optional;

public interface PersonRepository extends Repository<Person, Long> {
    boolean existsByName(String name);
    Optional<Person> findByName(String name);
}
