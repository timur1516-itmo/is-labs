package ru.itmo.se.is.feature.fileimport.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationLazyBeanParamDto;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationLazyResponseDto;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperationRepository;
import ru.itmo.se.is.feature.fileimport.infrastructure.mapper.ImportOperationMapper;
import ru.itmo.se.is.platform.db.eclipselink.tx.TransactionalMode;
import ru.itmo.se.is.platform.db.eclipselink.tx.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Transactional(mode = TransactionalMode.REQUIRES_NEW)
public class ImportOperationService {

    @Inject
    private ImportOperationRepository importOperationRepository;
    @Inject
    private ImportOperationMapper mapper;

    public ImportOperation saveImportOperation(ImportOperation importOperation) {
        return importOperationRepository.save(importOperation);
    }

    public ImportOperationLazyResponseDto lazyGet(@Valid ImportOperationLazyBeanParamDto lazyBeanParamDto) {
        Map<String, Object> filterBy = getFilterBy(lazyBeanParamDto);
        List<ImportOperation> data = importOperationRepository.load(
                lazyBeanParamDto.getFirst(),
                lazyBeanParamDto.getPageSize(),
                lazyBeanParamDto.getSortField(),
                lazyBeanParamDto.getSortOrder(),
                filterBy
        );
        long totalRecords = importOperationRepository.count(filterBy);
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
