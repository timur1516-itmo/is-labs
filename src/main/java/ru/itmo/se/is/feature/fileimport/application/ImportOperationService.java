package ru.itmo.se.is.feature.fileimport.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.itmo.se.is.feature.fileimport.api.dto.FileResponseDto;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationPagingAndSortingBeanParamDto;
import ru.itmo.se.is.feature.fileimport.api.dto.ImportOperationPagingAndSortingResponseDto;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperationRepository;
import ru.itmo.se.is.feature.fileimport.infrastructure.mapper.ImportOperationMapper;
import ru.itmo.se.is.shared.exception.EntityNotFoundException;
import ru.itmo.se.is.shared.storage.FileStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Transactional
public class ImportOperationService {

    @Inject
    private ImportOperationRepository importOperationRepository;
    @Inject
    private ImportOperationMapper importOperationMapper;
    @Inject
    private FileStorage fileStorage;

    public ImportOperation saveImportOperation(ImportOperation importOperation) {
        return importOperationRepository.save(importOperation);
    }

    public FileResponseDto getFile(long id) throws Exception {
        ImportOperation operation = getById(id);

        if (operation.getFileResource() == null)
            throw new EntityNotFoundException(String.format("File for operation with id %d not found", id));

        byte[] content = fileStorage.get(
                operation.getFileResource().getFileObjectKey()
        );
        String fileName = operation.getFileResource().getFileName();

        return new FileResponseDto(content, fileName);
    }

    public ImportOperationPagingAndSortingResponseDto getPagingAndSorting(ImportOperationPagingAndSortingBeanParamDto dto) {
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

    private Map<String, Object> getFilterBy(ImportOperationPagingAndSortingBeanParamDto dto) {
        Map<String, Object> filterBy = new HashMap<>();
        if (dto.getIdFilter() != null)
            filterBy.put("id", dto.getIdFilter());
        if (dto.getStatusFilter() != null)
            filterBy.put("status", dto.getStatusFilter());
        return filterBy;
    }

    public ImportOperation getById(long id) {
        return importOperationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Import operation with id " + id + " not found"));
    }
}
