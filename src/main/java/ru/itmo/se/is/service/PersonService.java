package ru.itmo.se.is.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.postgresql.util.PSQLException;
import ru.itmo.se.is.dto.person.PersonLazyBeanParamDto;
import ru.itmo.se.is.dto.person.PersonLazyResponseDto;
import ru.itmo.se.is.dto.person.PersonRequestDto;
import ru.itmo.se.is.dto.person.PersonResponseDto;
import ru.itmo.se.is.entity.Person;
import ru.itmo.se.is.exception.DeletionConflictException;
import ru.itmo.se.is.exception.EntityNotFoundException;
import ru.itmo.se.is.mapper.PersonMapper;
import ru.itmo.se.is.repository.EclipseLinkLazyPersonRepository;
import ru.itmo.se.is.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@ApplicationScoped
public class PersonService {
    @Inject
    private Repository<Person, Long> repository;

    @Inject
    private EclipseLinkLazyPersonRepository lazyRepository;

    @Inject
    private PersonMapper mapper;

    public PersonResponseDto create(PersonRequestDto dto) {
        return mapper.toDto(
                repository.save(
                        mapper.toPerson(dto)
                )
        );
    }

    public void update(long id, PersonRequestDto dto) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Person with id %d not found", id)));
        repository.update(person, (p) -> mapper.toPerson(dto, p));
    }

    public void delete(long id) {
        try {
            repository.deleteById(id);
        } catch (DatabaseException e) {
            Throwable internal = e.getInternalException();
            if (internal instanceof PSQLException psqlEx) {
                if ("23503".equals(psqlEx.getSQLState())) {
                    throw new DeletionConflictException(
                            String.format("Cannot delete Person with id %d, it is still referenced by Movies", id)
                    );
                }
            }
            throw e;
        }
    }

    public PersonLazyResponseDto lazyGet(PersonLazyBeanParamDto lazyBeanParamDto) {
        Map<String, Object> filterBy = getFilterBy(lazyBeanParamDto);

        List<Person> data = lazyRepository.load(
                lazyBeanParamDto.getFirst(),
                lazyBeanParamDto.getPageSize(),
                lazyBeanParamDto.getSortField(),
                lazyBeanParamDto.getSortOrder(),
                filterBy
        );
        long totalRecords = lazyRepository.count(filterBy);
        return new PersonLazyResponseDto(mapper.toDto(data), totalRecords);
    }

    private Map<String, Object> getFilterBy(PersonLazyBeanParamDto lazyBeanParamDto) {
        Map<String, Object> filterBy = new HashMap<>();
        if (lazyBeanParamDto.getNameFilter() != null)
            filterBy.put("name", lazyBeanParamDto.getNameFilter());
        if (lazyBeanParamDto.getEyeColorFilter() != null)
            filterBy.put("eyeColor", lazyBeanParamDto.getEyeColorFilter());
        if (lazyBeanParamDto.getHairColorFilter() != null)
            filterBy.put("hairColor", lazyBeanParamDto.getHairColorFilter());
        if (lazyBeanParamDto.getNationalityFilter() != null)
            filterBy.put("nationality", lazyBeanParamDto.getNationalityFilter());
        return filterBy;
    }

    public Person getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Person with id %d not found ", id)));
    }
}
