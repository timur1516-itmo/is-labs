package ru.itmo.se.is.feature.fileimport.application.saga.steps;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.itmo.se.is.feature.fileimport.application.ImportOperationService;
import ru.itmo.se.is.feature.fileimport.application.saga.SagaContext;
import ru.itmo.se.is.feature.fileimport.application.saga.SagaStep;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.ImportFileParserFactory;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;
import ru.itmo.se.is.feature.movie.application.MovieService;
import ru.itmo.se.is.platform.db.exception.annotation.TranslatePersistenceExceptions;
import ru.itmo.se.is.shared.exception.ImportDataValidationException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@ApplicationScoped
public class StoreObjectsSagaStep implements SagaStep {

    @Inject
    private ImportFileParserFactory importFileParserFactory;
    @Inject
    private MovieService movieService;
    @Inject
    private ImportOperationService importOperationService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @TranslatePersistenceExceptions
    public void execute(SagaContext context) {
        List<MovieRequestDto> importDto = importFileParserFactory
                .getParser(context.getFileExtension())
                .parse(new ByteArrayInputStream(context.getContent()));

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        List<ImportDataValidationException.ValidationError> errors =
                IntStream.range(0, importDto.size())
                        .boxed()
                        .flatMap(i -> validator
                                .validate(importDto.get(i))
                                .stream()
                                .map(cv -> new ImportDataValidationException.ValidationError(
                                        i,
                                        importDto.get(i).getClass().getName(),
                                        cv.getPropertyPath().toString(),
                                        cv.getMessage(),
                                        cv.getInvalidValue()
                                ))
                        )
                        .toList();
        factory.close();

        if (!errors.isEmpty())
            throw new ImportDataValidationException(errors);

        List<Long> savedObjects = new ArrayList<>();
        importDto.forEach(m -> {
            Long savedId = movieService.create(m).getId();
            savedObjects.add(savedId);
        });
        context.setImportedCount(importDto.size());
        context.setSavedObjectsIndexes(savedObjects);

        context.getImportOperation().setImportedCnt((long) importDto.size());
        context.setImportOperation(
                importOperationService.saveImportOperation(context.getImportOperation())
        );
    }

    @Override
    public void compensate(SagaContext context) {
        context.getSavedObjectsIndexes().forEach(movieService::delete);
        context.getImportOperation().setImportedCnt(null);
        context.setImportOperation(
                importOperationService.saveImportOperation(context.getImportOperation())
        );
    }
}
