package ru.itmo.se.is.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.itmo.se.is.annotation.MyTransactional;
import ru.itmo.se.is.dto.importing.FileUploadRequestDto;
import ru.itmo.se.is.dto.importing.ImportOperationResponseDto;
import ru.itmo.se.is.dto.movie.MovieRequestDto;
import ru.itmo.se.is.entity.ImportOperation;
import ru.itmo.se.is.entity.value.ImportStatus;
import ru.itmo.se.is.mapper.ImportOperationMapper;
import ru.itmo.se.is.parser.ImportFileParserFactory;
import ru.itmo.se.is.parser.MovieImportFileParser;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.List;

@ApplicationScoped
@MyTransactional
public class ImportService {

    @Inject
    private ImportFileParserFactory importFileParserFactory;
    @Inject
    private ImportOperationMapper importOperationMapper;
    @Inject
    private MovieService movieService;
    @Inject
    private ImportOperationService importOperationService;

    public ImportOperationResponseDto importMovies(FileUploadRequestDto requestDto) {
        ImportOperation importOperation = new ImportOperation();
        MovieImportFileParser parser = importFileParserFactory.getParser(requestDto.getFormat());
        InputStream inputStream = requestDto.getFileStream();
        importOperation.setStartDt(ZonedDateTime.now());
        try {
            long cnt = doImport(parser, inputStream);
            importOperation.setEndDt(ZonedDateTime.now());
            importOperation.setImportedCnt(cnt);
            importOperation.setStatus(ImportStatus.SUCCESS);
            return importOperationMapper.toDto(importOperationService.saveImportOperation(importOperation));
        } catch (Exception ex) {
            importOperation.setStatus(ImportStatus.FAILED);
            importOperation.setEndDt(ZonedDateTime.now());
            importOperation.setErrorMessage(ex.getMessage());
            importOperationService.saveImportOperation(importOperation);
            throw ex;
        }
    }

    public long doImport(MovieImportFileParser parser, InputStream inputStream) {
        List<MovieRequestDto> importDto = parser.parse(inputStream);
        importDto.forEach(m -> movieService.create(m));
        return importDto.size();
    }
}
