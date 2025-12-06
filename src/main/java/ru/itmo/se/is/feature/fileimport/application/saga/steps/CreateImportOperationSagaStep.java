package ru.itmo.se.is.feature.fileimport.application.saga.steps;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.itmo.se.is.feature.fileimport.application.ImportOperationService;
import ru.itmo.se.is.feature.fileimport.application.saga.SagaContext;
import ru.itmo.se.is.feature.fileimport.application.saga.SagaStep;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportOperationStatus;

import java.time.ZonedDateTime;

@ApplicationScoped
public class CreateImportOperationSagaStep implements SagaStep {

    @Inject
    private ImportOperationService importOperationService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void execute(SagaContext context) {
        ImportOperation importOperation = new ImportOperation();
        importOperation.setStartDt(ZonedDateTime.now());
        importOperation.setStatus(ImportOperationStatus.PENDING);

        context.setImportOperation(
                importOperationService.saveImportOperation(importOperation)
        );
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void compensate(SagaContext context) {
        ImportOperation importOperation = context.getImportOperation();
        if (importOperation == null) {
            return;
        }

        importOperation.setStatus(ImportOperationStatus.FAILED_INTERNAL);
        importOperation.setErrorMessage("Internal server error");
        importOperation.setEndDt(ZonedDateTime.now());

        context.setImportOperation(
                importOperationService.saveImportOperation(importOperation)
        );
    }
}
