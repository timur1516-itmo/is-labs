package ru.itmo.se.is.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import ru.itmo.se.is.db.UnitOfWorkManager;
import ru.itmo.se.is.entity.Movie;

@ApplicationScoped
@NoArgsConstructor
public class EclipseLinkLazyMovieRepository extends GenericEclipseLinkLazyRepository<Movie> {
    @Inject
    public EclipseLinkLazyMovieRepository(UnitOfWorkManager unitOfWorkManager) {
        super(Movie.class, unitOfWorkManager);
    }
}
