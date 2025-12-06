package ru.itmo.se.is.feature.fileimport.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.itmo.se.is.feature.fileimport.api.dto.FileRequestDto;
import ru.itmo.se.is.feature.fileimport.application.saga.SagaContext;
import ru.itmo.se.is.feature.fileimport.application.saga.SagaOrchestrator;
import ru.itmo.se.is.feature.fileimport.application.saga.steps.CreateImportOperationSagaStep;
import ru.itmo.se.is.feature.fileimport.application.saga.steps.FinalizeImportOperationSagaStep;
import ru.itmo.se.is.feature.fileimport.application.saga.steps.StoreFileSagaStep;
import ru.itmo.se.is.feature.fileimport.application.saga.steps.StoreObjectsSagaStep;
import ru.itmo.se.is.shared.notification.NotificationMessageType;
import ru.itmo.se.is.shared.notification.NotificationService;

import java.util.List;

@ApplicationScoped
public class ImportService {

    @Inject
    CreateImportOperationSagaStep createImportOperationSagaStep;
    @Inject
    private NotificationService notificationService;
    @Inject
    private SagaOrchestrator sagaOrchestrator;
    @Inject
    private StoreFileSagaStep storeFileSagaStep;
    @Inject
    private StoreObjectsSagaStep storeObjectsSagaStep;
    @Inject
    private FinalizeImportOperationSagaStep finalizeImportOperationSagaStep;

    public void importMovies(FileRequestDto requestDto) throws Exception {
        SagaContext context = new SagaContext();
        context.setFileName(requestDto.getFileName());
        context.setFileExtension(requestDto.getFileExtension());
        context.setContent(requestDto.getContent());

        try {
            sagaOrchestrator.executeSaga(
                    List.of(createImportOperationSagaStep,
                            storeFileSagaStep,
                            storeObjectsSagaStep,
                            finalizeImportOperationSagaStep),
                    context
            );
        } finally {
            notificationService.notifyAll(NotificationMessageType.IMPORT_OPERATION);
        }
    }
}
