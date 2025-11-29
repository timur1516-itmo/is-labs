package ru.itmo.se.is.feature.fileimport.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationPagingAndSortingBeanParamDto;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationPagingAndSortingResponseDto;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationResponseDto;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperationRepository;
import ru.itmo.se.is.feature.fileimport.infrastructure.mapper.ImportOperationMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class ImportOperationService {

    @Inject
    private ImportOperationRepository importOperationRepository;
    @Inject
    private ImportOperationMapper importOperationMapper;

    public ImportOperationResponseDto saveImportOperation(ImportOperation importOperation) {
        return importOperationMapper.toDto(
                importOperationRepository.save(importOperation)
        );
    }

    public ImportOperationPagingAndSortingResponseDto getPagingAndSorting(@Valid ImportOperationPagingAndSortingBeanParamDto dto) {
        Map<String, Object> filterBy = getFilterBy(dto);
        List<ImportOperation> data = importOperationRepository.load(
                dto.getFirst(),
                dto.getPageSize(),
                dto.getSortField(),
                dto.getSortOrder(),
                filterBy
        );
        long totalRecords = importOperationRepository.count(filterBy);
        return new ImportOperationPagingAndSortingResponseDto(importOperationMapper.toDto(data), totalRecords);
    }

    private Map<String, Object> getFilterBy(@Valid ImportOperationPagingAndSortingBeanParamDto dto) {
        Map<String, Object> filterBy = new HashMap<>();
        if (dto.getIdFilter() != null)
            filterBy.put("id", dto.getIdFilter());
        if (dto.getStatusFilter() != null)
            filterBy.put("status", dto.getStatusFilter());
        return filterBy;
    }
}
