package ru.itmo.se.is.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import ru.itmo.se.is.annotation.MyTransactional;
import ru.itmo.se.is.dto.person.PersonLazyBeanParamDto;
import ru.itmo.se.is.dto.person.PersonLazyResponseDto;
import ru.itmo.se.is.dto.person.PersonRequestDto;
import ru.itmo.se.is.entity.Person;
import ru.itmo.se.is.exception.BusinessUniqueConstraintException;
import ru.itmo.se.is.exception.EntityNotFoundException;
import ru.itmo.se.is.mapper.PersonMapper;
import ru.itmo.se.is.repository.EclipseLinkLazyPersonRepository;
import ru.itmo.se.is.repository.PersonRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MyTransactional
@ApplicationScoped
public class PersonService {
    @Inject
    private PersonRepository personRepository;

    @Inject
    private EclipseLinkLazyPersonRepository lazyRepository;

    @Inject
    private PersonMapper mapper;

    public Person create(@Valid PersonRequestDto dto) {
        Person person = mapper.toPerson(dto);

        checkUniqueConstraint(person);
        Person savedPerson = personRepository.save(person);

        return savedPerson;
    }

    public Person createOrGetExisting(@Valid PersonRequestDto dto) {
        return personRepository.findByName(dto.getName())
                .orElseGet(() -> {
                    Person person = mapper.toPerson(dto);
                    Person savedPerson = personRepository.save(person);
                    return savedPerson;
                });
    }

    public void update(long id, @Valid PersonRequestDto dto) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Person with id %d not found", id)));
        Person updatedPerson = mapper.toPerson(dto);

        checkUniqueConstraint(updatedPerson);
        personRepository.update(person, (p) -> mapper.toPerson(dto, p));
    }

    private void checkUniqueConstraint(Person person) {
        if (personRepository.existsByName(person.getName())) {
            throw new BusinessUniqueConstraintException(
                    String.format("Person with name %s already exists", person.getName())
            );
        }
    }

    public void delete(long id) {
        personRepository.deleteById(id);
    }

    public PersonLazyResponseDto lazyGet(@Valid PersonLazyBeanParamDto lazyBeanParamDto) {
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

    private Map<String, Object> getFilterBy(@Valid PersonLazyBeanParamDto lazyBeanParamDto) {
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
        return personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Person with id %d not found ", id)));
    }
}
