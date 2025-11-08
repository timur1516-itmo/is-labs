package ru.itmo.se.is.parser.json;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import ru.itmo.se.is.dto.importing.ImportFileFormat;
import ru.itmo.se.is.dto.movie.MovieRequestDto;
import ru.itmo.se.is.mapper.RawJsonMapper;
import ru.itmo.se.is.mapper.RawJsonMapperImpl;
import ru.itmo.se.is.parser.MovieImportFileParser;

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
