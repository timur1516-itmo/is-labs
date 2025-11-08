package ru.itmo.se.is.parser.csv;

import jakarta.enterprise.context.ApplicationScoped;
import ru.itmo.se.is.dto.importing.ImportFileFormat;
import ru.itmo.se.is.dto.movie.MovieRequestDto;
import ru.itmo.se.is.parser.MovieImportFileParser;

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
