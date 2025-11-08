package ru.itmo.se.is.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import ru.itmo.se.is.annotation.MyTransactional;
import ru.itmo.se.is.db.MyTransactionMode;
import ru.itmo.se.is.dto.importing.ImportOperationLazyBeanParamDto;
import ru.itmo.se.is.dto.importing.ImportOperationLazyResponseDto;
import ru.itmo.se.is.entity.ImportOperation;
import ru.itmo.se.is.mapper.ImportOperationMapper;
import ru.itmo.se.is.repository.EclipseLinkLazyImportOperationRepository;
import ru.itmo.se.is.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@MyTransactional(mode = MyTransactionMode.REQUIRES_NEW)
public class ImportOperationService {

    @Inject
    private Repository<ImportOperation, Long> importOperationRepository;
    @Inject
    private EclipseLinkLazyImportOperationRepository lazyRepository;
    @Inject
    private ImportOperationMapper mapper;

    public ImportOperation saveImportOperation(ImportOperation importOperation) {
        return importOperationRepository.save(importOperation);
    }

    public ImportOperationLazyResponseDto lazyGet(@Valid ImportOperationLazyBeanParamDto lazyBeanParamDto) {
        Map<String, Object> filterBy = getFilterBy(lazyBeanParamDto);
        List<ImportOperation> data = lazyRepository.load(
                lazyBeanParamDto.getFirst(),
                lazyBeanParamDto.getPageSize(),
                lazyBeanParamDto.getSortField(),
                lazyBeanParamDto.getSortOrder(),
                filterBy
        );
        long totalRecords = lazyRepository.count(filterBy);
        return new ImportOperationLazyResponseDto(mapper.toDto(data), totalRecords);
    }

    private Map<String, Object> getFilterBy(@Valid ImportOperationLazyBeanParamDto lazyBeanParamDto) {
        Map<String, Object> filterBy = new HashMap<>();
        if (lazyBeanParamDto.getIdFilter() != null)
            filterBy.put("id", lazyBeanParamDto.getIdFilter());
        if (lazyBeanParamDto.getStatusFilter() != null)
            filterBy.put("status", lazyBeanParamDto.getStatusFilter());
        return filterBy;
    }
}
