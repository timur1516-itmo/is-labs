package ru.itmo.se.is.feature.fileimport.infrastructure.parser.csv;

import jakarta.enterprise.context.ApplicationScoped;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportFileFormat;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.MovieImportFileParser;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;

import java.io.InputStream;
import java.util.List;

@ApplicationScoped
public class CsvMovieImportFileParser implements MovieImportFileParser {
    @Override
    public List<MovieRequestDto> parse(InputStream inputStream) {
        return List.of();
    }

    @Override
    public boolean supports(ImportFileFormat importFileFormat) {
        return ImportFileFormat.CSV.equals(importFileFormat);
    }
}
