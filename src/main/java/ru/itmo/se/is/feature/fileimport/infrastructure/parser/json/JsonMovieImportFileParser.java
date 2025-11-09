package ru.itmo.se.is.feature.fileimport.infrastructure.parser.json;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportFileFormat;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.MovieImportFileParser;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.json.mapper.RawJsonMapper;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.json.raw.RawJsonMovie;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class JsonMovieImportFileParser implements MovieImportFileParser {
    @Inject
    private RawJsonMapper rawJsonMapper;

    @Override
    public List<MovieRequestDto> parse(InputStream inputStream) {
        Jsonb jsonb = JsonbBuilder.create();
        RawJsonMovie[] arr = jsonb.fromJson(inputStream, RawJsonMovie[].class);
        return rawJsonMapper.toMovieRequestDto(Arrays.asList(arr));
    }

    @Override
    public boolean supports(ImportFileFormat importFileFormat) {
        return ImportFileFormat.JSON.equals(importFileFormat);
    }
}
