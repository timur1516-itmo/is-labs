package ru.itmo.se.is.feature.fileimport.infrastructure.parser;

import ru.itmo.se.is.feature.fileimport.domain.value.FileExtension;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;

import java.io.InputStream;
import java.util.List;

public interface MovieImportFileParser {
    List<MovieRequestDto> parse(InputStream inputStream);

    boolean supports(FileExtension fileExtension);
}
