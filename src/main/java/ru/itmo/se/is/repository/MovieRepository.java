package ru.itmo.se.is.repository;

import ru.itmo.se.is.entity.Movie;

public interface MovieRepository extends Repository<Movie, Long> {
    boolean existsByNameAndDirectorName(String name, String directorName);
}
