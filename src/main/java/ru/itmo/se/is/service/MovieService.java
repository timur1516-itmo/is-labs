package ru.itmo.se.is.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.itmo.se.is.dto.movie.*;
import ru.itmo.se.is.dto.person.PersonResponseDto;
import ru.itmo.se.is.entity.Movie;
import ru.itmo.se.is.entity.Person;
import ru.itmo.se.is.entity.value.MpaaRating;
import ru.itmo.se.is.exception.EntityNotFoundException;
import ru.itmo.se.is.mapper.MovieMapper;
import ru.itmo.se.is.mapper.PersonMapper;
import ru.itmo.se.is.repository.EclipseLinkLazyMovieRepository;
import ru.itmo.se.is.repository.Repository;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional
@ApplicationScoped
public class MovieService {

    @Inject
    private Repository<Movie, Long> repository;

    @Inject
    private EclipseLinkLazyMovieRepository lazyRepository;

    @Inject
    private MovieMapper mapper;

    @Inject
    private PersonMapper personMapper;

    public MovieResponseDto create(MovieRequestDto dto) {
        Movie movie = mapper.toMovie(dto);
        movie.setCreationDate(ZonedDateTime.now());
        return mapper.toDto(
                repository.save(
                        movie
                )
        );
    }

    public void update(long id, MovieRequestDto dto) {
        Movie movie = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Movie with id %d not found", id)));
        repository.update(movie, (m) -> mapper.toMovie(dto, m));
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public MovieLazyResponseDto lazyGet(MovieLazyBeanParamDto lazyBeanParamDto) {
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

    private Map<String, Object> getFilterBy(MovieLazyBeanParamDto lazyBeanParamDto) {
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
        Long count = repository.findAll().stream()
                .map(Movie::getTagline)
                .filter(t -> t.equals(tagline))
                .count();
        return new MovieCountResponseDto(count);
    }

    public MovieCountResponseDto countLessThanGoldenPalm(long baseCount) {
        Long count = repository.findAll().stream()
                .map(Movie::getGoldenPalmCount)
                .filter(c -> c < baseCount)
                .count();
        return new MovieCountResponseDto(count);
    }

    public MovieCountResponseDto countGreaterThanGoldenPalm(long baseCount) {
        Long count = repository.findAll().stream()
                .map(Movie::getGoldenPalmCount)
                .filter(c -> c > baseCount)
                .count();
        return new MovieCountResponseDto(count);
    }

    public List<PersonResponseDto> getDirectorsWithoutOscars() {
        List<Movie> movies = repository.findAll();
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
        repository.findAll().stream()
                .filter(m -> Objects.equals(m.getMpaaRating(), (MpaaRating.R)))
                .forEach(m -> {
                    repository.update(m, (mv) -> mv.setOscarsCount(m.getOscarsCount() + 1));
                });
    }
}
