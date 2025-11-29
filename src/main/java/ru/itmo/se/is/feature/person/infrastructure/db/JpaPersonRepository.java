package ru.itmo.se.is.feature.person.infrastructure.db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.NoArgsConstructor;
import ru.itmo.se.is.feature.person.domain.Person;
import ru.itmo.se.is.feature.person.domain.PersonRepository;
import ru.itmo.se.is.platform.db.jpa.JpaPagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class JpaPersonRepository
        extends JpaPagingAndSortingRepository<Person, Long>
        implements PersonRepository {

    @Inject
    public JpaPersonRepository(EntityManager entityManager) {
        super(Person.class, entityManager);
    }

    @Override
    public boolean existsByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Person> person = cq.from(Person.class);

        cq.select(cb.count(person))
                .where(cb.equal(person.get("name"), name));

        Long count = em.createQuery(cq).getSingleResult();
        return count > 0;
    }

    @Override
    public Optional<Person> findByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> person = cq.from(Person.class);

        cq.select(person)
                .where(cb.equal(person.get("name"), name));

        List<Person> result = em.createQuery(cq).getResultList();
        return result.stream().findFirst();
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Person> person = cq.from(Person.class);

        cq.select(cb.count(person))
                .where(
                        cb.and(
                                cb.equal(person.get("name"), name),
                                cb.notEqual(person.get("id"), id)
                        )
                );

        Long count = em.createQuery(cq).getSingleResult();
        return count > 0;
    }
}
