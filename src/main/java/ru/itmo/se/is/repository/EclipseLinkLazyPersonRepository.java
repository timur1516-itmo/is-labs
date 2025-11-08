package ru.itmo.se.is.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.server.Server;
import ru.itmo.se.is.db.UnitOfWorkManager;
import ru.itmo.se.is.entity.Person;

@ApplicationScoped
@NoArgsConstructor
public class EclipseLinkLazyPersonRepository extends GenericEclipseLinkLazyRepository<Person> {
    @Inject
    public EclipseLinkLazyPersonRepository(UnitOfWorkManager unitOfWorkManager) {
        super(Person.class, unitOfWorkManager);
    }
}
