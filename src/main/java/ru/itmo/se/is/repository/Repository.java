package ru.itmo.se.is.repository;

import org.eclipse.persistence.sessions.UnitOfWork;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface Repository<T, ID> {
    Optional<T> findById(ID id);

    List<T> findAll();

    T save(T entity);

    void update(T entity, Consumer<T> fieldUpdater);

    void deleteById(ID id);
}
