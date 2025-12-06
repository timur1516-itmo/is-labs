package ru.itmo.se.is.feature.fileimport.application.saga.steps;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.itmo.se.is.feature.fileimport.application.ImportOperationService;
import ru.itmo.se.is.feature.fileimport.application.saga.SagaContext;
import ru.itmo.se.is.feature.fileimport.application.saga.SagaStep;
import ru.itmo.se.is.feature.fileimport.domain.FileResource;
import ru.itmo.se.is.shared.storage.FileStorage;

import java.util.UUID;

@ApplicationScoped
public class StoreFileSagaStep implements SagaStep {

    @Inject
    private FileStorage fileStorage;
    @Inject
    private ImportOperationService importOperationService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void execute(SagaContext context) throws Exception {
        String objectKey = UUID.randomUUID() + "_" + context.getFileName();
        fileStorage.save(objectKey, context.getContent());
        context.setObjectKey(objectKey);

        FileResource fileResource = new FileResource();
        fileResource.setFileName(context.getFileName());
        fileResource.setFileExtension(context.getFileExtension());
        fileResource.setFileObjectKey(context.getObjectKey());
        context.getImportOperation().setFileResource(fileResource);
        context.setImportOperation(
                importOperationService.saveImportOperation(context.getImportOperation())
        );
    }

    @Override
    public void compensate(SagaContext context) throws Exception {
        if (context.getObjectKey() == null) return;
        fileStorage.delete(context.getObjectKey());
        if (context.getImportOperation().getFileResource() != null) {
            context.getImportOperation().setFileResource(null);
            context.setImportOperation(
                    importOperationService.saveImportOperation(context.getImportOperation())
            );
        }
    }
}
