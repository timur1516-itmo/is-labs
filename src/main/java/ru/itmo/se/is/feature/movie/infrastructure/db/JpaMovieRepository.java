package ru.itmo.se.is.feature.movie.infrastructure.db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NoArgsConstructor;
import ru.itmo.se.is.feature.movie.domain.Movie;
import ru.itmo.se.is.feature.movie.domain.MovieRepository;
import ru.itmo.se.is.platform.db.jpa.JpaPagingAndSortingRepository;

@ApplicationScoped
@NoArgsConstructor
public class JpaMovieRepository
        extends JpaPagingAndSortingRepository<Movie, Long>
        implements MovieRepository {

    @Inject
    public JpaMovieRepository(EntityManager entityManager) {
        super(Movie.class, entityManager);
    }

    @Override
    public boolean existsByNameAndDirectorName(String name, String directorName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Movie> movie = cq.from(Movie.class);

        Predicate predicate = cb.and(
                cb.equal(movie.get("name"), name),
                cb.equal(movie.get("director").get("name"), directorName)
        );

        cq.select(cb.count(movie)).where(predicate);

        Long count = em.createQuery(cq).getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByNameAndDirectorNameAndIdNot(String name, String directorName, Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Movie> movie = cq.from(Movie.class);

        Predicate predicate = cb.and(
                cb.equal(movie.get("name"), name),
                cb.equal(movie.get("director").get("name"), directorName),
                cb.notEqual(movie.get("id"), id)
        );

        cq.select(cb.count(movie)).where(predicate);

        Long count = em.createQuery(cq).getSingleResult();
        return count > 0;
    }
}
