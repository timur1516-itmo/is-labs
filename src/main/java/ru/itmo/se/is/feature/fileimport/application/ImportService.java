package ru.itmo.se.is.feature.fileimport.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.itmo.se.is.feature.fileimport.api.dto.FileUploadRequestDto;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportStatus;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.ImportFileParserFactory;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.MovieImportFileParser;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;
import ru.itmo.se.is.feature.movie.application.MovieService;
import ru.itmo.se.is.platform.storage.StoredFile;
import ru.itmo.se.is.shared.notification.NotificationMessageType;
import ru.itmo.se.is.shared.notification.NotificationService;
import ru.itmo.se.is.shared.storage.FileStorage;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class ImportService {

    @Inject
    private ImportFileParserFactory importFileParserFactory;
    @Inject
    private MovieService movieService;
    @Inject
    private ImportOperationService importOperationService;
    @Inject
    private NotificationService notificationService;
    @Inject
    private FileStorage fileStorage;

    public void importMovies(FileUploadRequestDto requestDto) {
        ImportOperation importOperation = new ImportOperation();
        MovieImportFileParser parser = importFileParserFactory.getParser(requestDto.getFormat());
        importOperation.setStartDt(ZonedDateTime.now());

        StoredFile storedFile = fileStorage.save(
                requestDto.getFileName(),
                requestDto.getFileStream()
        );

        importOperation.setFileName(storedFile.getOriginalFileName());
        importOperation.setFileBucket(storedFile.getBucket());
        importOperation.setFileObjectKey(storedFile.getObjectKey());

        try {
            InputStream parsingStream =
                    fileStorage.get(storedFile.getBucket(), storedFile.getObjectKey());
            long cnt = doImport(parser, parsingStream);
            importOperation.setEndDt(ZonedDateTime.now());
            importOperation.setImportedCnt(cnt);
            importOperation.setStatus(ImportStatus.SUCCESS);
            importOperationService.saveImportOperation(importOperation);
        } catch (Exception ex) {
            importOperation.setStatus(ImportStatus.FAILED);
            importOperation.setEndDt(ZonedDateTime.now());
            importOperation.setErrorMessage(ex.getMessage());
            importOperationService.saveImportOperation(importOperation);
            throw ex;
        } finally {
            notificationService.notifyAll(NotificationMessageType.IMPORT_OPERATION);
        }
    }

    public long doImport(MovieImportFileParser parser, InputStream inputStream) {
        List<MovieRequestDto> importDto = parser.parse(inputStream);
        importDto.forEach(m -> movieService.create(m));
        return importDto.size();
    }
}
