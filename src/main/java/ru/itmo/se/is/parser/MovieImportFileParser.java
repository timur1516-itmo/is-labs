package ru.itmo.se.is.parser;

import ru.itmo.se.is.dto.importing.ImportFileFormat;
import ru.itmo.se.is.dto.movie.MovieRequestDto;

import java.io.InputStream;
import java.util.List;

public interface MovieImportFileParser {
    List<MovieRequestDto> parse(InputStream inputStream);
    boolean supports(ImportFileFormat importFileFormat);
}
