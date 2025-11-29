package ru.itmo.se.is.platform.db.jpa;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagerProvider {
    @Produces
    @PersistenceContext(unitName = "my-persistence-unit")
    @Dependent
    private EntityManager entityManager;
}
