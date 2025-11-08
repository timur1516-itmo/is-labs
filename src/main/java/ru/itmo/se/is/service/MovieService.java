package ru.itmo.se.is.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import ru.itmo.se.is.annotation.MyTransactional;
import ru.itmo.se.is.dto.movie.MovieCountResponseDto;
import ru.itmo.se.is.dto.movie.MovieLazyBeanParamDto;
import ru.itmo.se.is.dto.movie.MovieLazyResponseDto;
import ru.itmo.se.is.dto.movie.MovieRequestDto;
import ru.itmo.se.is.dto.person.PersonResponseDto;
import ru.itmo.se.is.entity.Movie;
import ru.itmo.se.is.entity.Person;
import ru.itmo.se.is.entity.value.MpaaRating;
import ru.itmo.se.is.exception.BusinessUniqueConstraintException;
import ru.itmo.se.is.exception.EntityNotFoundException;
import ru.itmo.se.is.mapper.MovieMapper;
import ru.itmo.se.is.mapper.PersonMapper;
import ru.itmo.se.is.repository.EclipseLinkLazyMovieRepository;
import ru.itmo.se.is.repository.MovieRepository;
import ru.itmo.se.is.repository.PersonRepository;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@MyTransactional
@ApplicationScoped
public class MovieService {

    @Inject
    private MovieRepository movieRepository;

    @Inject
    private EclipseLinkLazyMovieRepository lazyRepository;

    @Inject
    private MovieMapper mapper;

    @Inject
    private PersonMapper personMapper;

    @Inject
    private PersonService personService;

    public Movie create(@Valid MovieRequestDto dto) {
        Movie movie = mapper.toMovie(dto);

        if (dto.getDirectorReference().isNew()) {
            movie.setDirector(personService.createOrGetExisting(dto.getDirectorReference().getValue()));
        }
        if (dto.getOperatorReference().isNew()) {
            movie.setOperator(personService.createOrGetExisting(dto.getOperatorReference().getValue()));
        }
        if (dto.getScreenwriterReference().isNew()) {
            movie.setScreenwriter(personService.createOrGetExisting(dto.getScreenwriterReference().getValue()));
        }

        movie.setCreationDate(ZonedDateTime.now());

        checkUniqueConstraint(movie);
        Movie savedMovie = movieRepository.save(movie);

        return savedMovie;
    }

    public void update(long id, @Valid MovieRequestDto dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Movie with id %d not found", id)));
        Movie updatedMovie = mapper.toMovie(dto);

        checkUniqueConstraint(updatedMovie);
        movieRepository.update(movie, (m) -> mapper.toMovie(dto, m));
    }

    private void checkUniqueConstraint(Movie movie){
        if (movieRepository.existsByNameAndDirectorName(movie.getName(), movie.getDirector().getName())) {
            throw new BusinessUniqueConstraintException(
                    String.format("Movie with name '%s' and director '%s' already exists",
                            movie.getName(),
                            movie.getDirector().getName()
                    )
            );
        }
    }

    public void delete(long id) {
        movieRepository.deleteById(id);
    }

    public MovieLazyResponseDto lazyGet(@Valid MovieLazyBeanParamDto lazyBeanParamDto) {
        Map<String, Object> filterBy = getFilterBy(lazyBeanParamDto);

        List<Movie> data = lazyRepository.load(
                lazyBeanParamDto.getFirst(),
                lazyBeanParamDto.getPageSize(),
                lazyBeanParamDto.getSortField(),
                lazyBeanParamDto.getSortOrder(),
                filterBy
        );
        long totalRecords = lazyRepository.count(filterBy);
        return new MovieLazyResponseDto(mapper.toDto(data), totalRecords);
    }

    private Map<String, Object> getFilterBy(@Valid MovieLazyBeanParamDto lazyBeanParamDto) {
        Map<String, Object> filterBy = new HashMap<>();
        if (lazyBeanParamDto.getIdFilter() != null)
            filterBy.put("id", lazyBeanParamDto.getIdFilter());
        if (lazyBeanParamDto.getNameFilter() != null)
            filterBy.put("name", lazyBeanParamDto.getNameFilter());
        if (lazyBeanParamDto.getGenreFilter() != null)
            filterBy.put("genre", lazyBeanParamDto.getGenreFilter());
        if (lazyBeanParamDto.getMpaaRatingFilter() != null)
            filterBy.put("mpaaRating", lazyBeanParamDto.getMpaaRatingFilter());
        if (lazyBeanParamDto.getTaglineFilter() != null)
            filterBy.put("tagline", lazyBeanParamDto.getTaglineFilter());
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
                    movieRepository.update(m, (mv) -> mv.setOscarsCount(m.getOscarsCount() + 1));
                });
    }
}
