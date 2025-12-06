package ru.itmo.se.is.feature.movie.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.itmo.se.is.feature.movie.api.dto.MovieCountResponseDto;
import ru.itmo.se.is.feature.movie.api.dto.MoviePagingAndSortingBeanParamDto;
import ru.itmo.se.is.feature.movie.api.dto.MoviePagingAndSortingResponseDto;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;
import ru.itmo.se.is.feature.movie.domain.Movie;
import ru.itmo.se.is.feature.movie.domain.MovieRepository;
import ru.itmo.se.is.feature.movie.domain.value.MpaaRating;
import ru.itmo.se.is.feature.movie.infrastructure.mapper.MovieMapper;
import ru.itmo.se.is.feature.person.api.dto.PersonResponseDto;
import ru.itmo.se.is.feature.person.application.PersonService;
import ru.itmo.se.is.feature.person.domain.Person;
import ru.itmo.se.is.feature.person.infrastructure.mapper.PersonMapper;
import ru.itmo.se.is.platform.db.exception.annotation.TranslatePersistenceExceptions;
import ru.itmo.se.is.shared.exception.EntityAlreadyExistsException;
import ru.itmo.se.is.shared.exception.EntityNotFoundException;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional
@TranslatePersistenceExceptions
@ApplicationScoped
public class MovieService {

    @Inject
    private MovieRepository movieRepository;

    @Inject
    private MovieMapper movieMapper;

    @Inject
    private PersonMapper personMapper;

    @Inject
    private PersonService personService;

    public Movie create(MovieRequestDto dto) {
        Movie movie = movieMapper.toMovie(dto);
        setEmbeddedFields(movie, dto);
        movie.setCreationDate(ZonedDateTime.now());
        checkCreateUniqueConstraint(movie);
        return movieRepository.save(movie);
    }

    public void update(long id, MovieRequestDto dto) {
        Movie movie = getById(id);
        setEmbeddedFields(movie, dto);
        movieMapper.toMovie(dto, movie);
        checkUpdateUniqueConstraint(movie);
        movieRepository.save(movie);
    }

    public Movie getById(long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Movie with id %d not found", id)));
    }

    private void setEmbeddedFields(Movie movie, MovieRequestDto dto) {
        movie.setDirector(dto.getDirectorReference().isNew() ?
                personService.createOrGetExisting(dto.getDirectorReference().getValue()) :
                personService.getById(dto.getDirectorReference().getId())
        );
        movie.setOperator(dto.getOperatorReference().isNew() ?
                personService.createOrGetExisting(dto.getOperatorReference().getValue()) :
                personService.getById(dto.getOperatorReference().getId())
        );
        movie.setScreenwriter(dto.getScreenwriterReference().isNew() ?
                personService.createOrGetExisting(dto.getScreenwriterReference().getValue()) :
                personService.getById(dto.getScreenwriterReference().getId())
        );
    }

    private void checkCreateUniqueConstraint(Movie movie) {
        if (movieRepository.existsByNameAndDirectorName(movie.getName(), movie.getDirector().getName())) {
            throw new EntityAlreadyExistsException(
                    String.format("Movie with name '%s' and director '%s' already exists",
                            movie.getName(),
                            movie.getDirector().getName()
                    )
            );
        }
    }

    private void checkUpdateUniqueConstraint(Movie movie) {
        if (movieRepository.existsByNameAndDirectorNameAndIdNot(movie.getName(), movie.getDirector().getName(), movie.getId())) {
            throw new EntityAlreadyExistsException(
                    String.format("Movie with name '%s' and director '%s' already exists",
                            movie.getName(),
                            movie.getDirector().getName()
                    )
            );
        }
    }

    public void delete(long id) {
        movieRepository.delete(getById(id));
    }

    public MoviePagingAndSortingResponseDto getPagingAndSorting(MoviePagingAndSortingBeanParamDto dto) {
        Map<String, Object> filterBy = getFilterBy(dto);

        List<Movie> data = movieRepository.load(
                dto.getFirst(),
                dto.getPageSize(),
                dto.getSortField(),
                dto.getSortOrder(),
                filterBy
        );
        long totalRecords = movieRepository.count(filterBy);
        return new MoviePagingAndSortingResponseDto(movieMapper.toDto(data), totalRecords);
    }

    private Map<String, Object> getFilterBy(MoviePagingAndSortingBeanParamDto dto) {
        Map<String, Object> filterBy = new HashMap<>();
        if (dto.getIdFilter() != null)
            filterBy.put("id", dto.getIdFilter());
        if (dto.getNameFilter() != null)
            filterBy.put("name", dto.getNameFilter());
        if (dto.getGenreFilter() != null)
            filterBy.put("genre", dto.getGenreFilter());
        if (dto.getMpaaRatingFilter() != null)
            filterBy.put("mpaaRating", dto.getMpaaRatingFilter());
        if (dto.getTaglineFilter() != null)
            filterBy.put("tagline", dto.getTaglineFilter());
        return filterBy;
    }

    public MovieCountResponseDto countByTagline(String tagline) {
        Long count = movieRepository.findAll().stream()
                .map(Movie::getTagline)
                .filter(t -> t.equals(tagline))
                .count();
        return new MovieCountResponseDto(count);
    }

    public MovieCountResponseDto countLessThanGoldenPalm(long baseCount) {
        Long count = movieRepository.findAll().stream()
                .map(Movie::getGoldenPalmCount)
                .filter(c -> c < baseCount)
                .count();
        return new MovieCountResponseDto(count);
    }

    public MovieCountResponseDto countGreaterThanGoldenPalm(long baseCount) {
        Long count = movieRepository.findAll().stream()
                .map(Movie::getGoldenPalmCount)
                .filter(c -> c > baseCount)
                .count();
        return new MovieCountResponseDto(count);
    }

    public List<PersonResponseDto> getDirectorsWithoutOscars() {
        List<Movie> movies = movieRepository.findAll();
        List<Person> directors = movies.stream()
                .filter(m -> m.getOscarsCount() == 0)
                .map(Movie::getDirector)
                .filter(d -> movies.stream()
                        .filter(m -> m.getDirector().equals(d))
                        .allMatch(m -> m.getOscarsCount() == 0))
                .distinct()
                .toList();
        return personMapper.toDto(directors);
    }

    public void addOscarToRated() {
        movieRepository.findAll().stream()
                .filter(m -> Objects.equals(m.getMpaaRating(), (MpaaRating.R)))
                .forEach(m -> {
                    m.setOscarsCount(m.getOscarsCount() + 1);
                    movieRepository.save(m);
                });
    }
}
