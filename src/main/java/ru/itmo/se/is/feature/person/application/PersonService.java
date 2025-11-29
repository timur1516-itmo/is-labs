package ru.itmo.se.is.feature.person.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import ru.itmo.se.is.feature.person.api.dto.PersonPagingAndSortingBeanParamDto;
import ru.itmo.se.is.feature.person.api.dto.PersonPagingAndSortingResponseDto;
import ru.itmo.se.is.feature.person.api.dto.PersonRequestDto;
import ru.itmo.se.is.feature.person.api.dto.PersonResponseDto;
import ru.itmo.se.is.feature.person.domain.Person;
import ru.itmo.se.is.feature.person.domain.PersonRepository;
import ru.itmo.se.is.feature.person.infrastructure.mapper.PersonMapper;
import ru.itmo.se.is.shared.exception.EntityAlreadyExistsException;
import ru.itmo.se.is.shared.exception.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@ApplicationScoped
public class PersonService {
    @Inject
    private PersonRepository personRepository;

    @Inject
    private PersonMapper personMapper;

    public PersonResponseDto create(@Valid PersonRequestDto dto) {
        Person person = personMapper.toPerson(dto);
        checkCreateUniqueConstraint(person);
        return personMapper.toDto(personRepository.save(person));
    }

    public Person createOrGetExisting(@Valid PersonRequestDto dto) {
        return personRepository.findByName(dto.getName())
                .orElseGet(() -> {
                    Person person = personMapper.toPerson(dto);
                    return personRepository.save(person);
                });
    }

    public void update(long id, @Valid PersonRequestDto dto) {
        Person person = getById(id);
        personMapper.toPerson(dto, person);
        checkUpdateUniqueConstraint(person);
        personRepository.save(person);
    }

    private void checkCreateUniqueConstraint(Person person) {
        if (personRepository.existsByName(person.getName())) {
            throw new EntityAlreadyExistsException(
                    String.format("Person with name %s already exists", person.getName())
            );
        }
    }

    private void checkUpdateUniqueConstraint(Person person) {
        if (personRepository.existsByNameAndIdNot(person.getName(), person.getId())) {
            throw new EntityAlreadyExistsException(
                    String.format("Person with name %s already exists", person.getName())
            );
        }
    }

    public void delete(long id) {
        personRepository.delete(getById(id));
    }

    public PersonPagingAndSortingResponseDto getPagingAndSorting(@Valid PersonPagingAndSortingBeanParamDto dto) {
        Map<String, Object> filterBy = getFilterBy(dto);

        List<Person> data = personRepository.load(
                dto.getFirst(),
                dto.getPageSize(),
                dto.getSortField(),
                dto.getSortOrder(),
                filterBy
        );
        long totalRecords = personRepository.count(filterBy);
        return new PersonPagingAndSortingResponseDto(personMapper.toDto(data), totalRecords);
    }

    private Map<String, Object> getFilterBy(@Valid PersonPagingAndSortingBeanParamDto dto) {
        Map<String, Object> filterBy = new HashMap<>();
        if (dto.getNameFilter() != null)
            filterBy.put("name", dto.getNameFilter());
        if (dto.getEyeColorFilter() != null)
            filterBy.put("eyeColor", dto.getEyeColorFilter());
        if (dto.getHairColorFilter() != null)
            filterBy.put("hairColor", dto.getHairColorFilter());
        if (dto.getNationalityFilter() != null)
            filterBy.put("nationality", dto.getNationalityFilter());
        return filterBy;
    }

    public Person getById(long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Person with id %d not found ", id)));
    }
}
