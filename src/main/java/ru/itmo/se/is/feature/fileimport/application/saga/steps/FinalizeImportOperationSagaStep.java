package ru.itmo.se.is.feature.fileimport.application.saga.steps;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.itmo.se.is.feature.fileimport.application.ImportOperationService;
import ru.itmo.se.is.feature.fileimport.application.saga.SagaContext;
import ru.itmo.se.is.feature.fileimport.application.saga.SagaStep;
import ru.itmo.se.is.feature.fileimport.domain.FileResource;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportOperationStatus;

import java.time.ZonedDateTime;

@ApplicationScoped
public class FinalizeImportOperationSagaStep implements SagaStep {

    @Inject
    private ImportOperationService importOperationService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void execute(SagaContext context) {
        ImportOperation importOperation = context.getImportOperation();
        if (importOperation == null) {
            throw new IllegalStateException("ImportOperation not initialized in saga context");
        }

        if (context.getError() == null) {
            importOperation.setStatus(ImportOperationStatus.SUCCESS);
            importOperation.setImportedCnt(context.getImportedCount());
        } else {
            importOperation.setStatus(ImportOperationStatus.FAILED);
            importOperation.setImportedCnt(null);
            importOperation.setErrorMessage(context.getError().getMessage());
        }
        attachFile(importOperation, context);
        importOperation.setEndDt(ZonedDateTime.now());

        context.setImportOperation(
                importOperationService.saveImportOperation(importOperation)
        );
    }

    @Override
    public void compensate(SagaContext context) {
    }

    private void attachFile(ImportOperation importOperation, SagaContext context) {
        FileResource fileResource = new FileResource();
        fileResource.setFileName(context.getFileName());
        fileResource.setFileExtension(context.getFileExtension());
        fileResource.setFileObjectKey(context.getObjectKey());
        importOperation.setFileResource(fileResource);
    }
}
