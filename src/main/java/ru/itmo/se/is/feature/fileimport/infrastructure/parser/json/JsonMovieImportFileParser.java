package ru.itmo.se.is.feature.fileimport.infrastructure.parser.json;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.stream.JsonParsingException;
import ru.itmo.se.is.feature.fileimport.domain.value.FileExtension;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.MovieImportFileParser;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.json.mapper.RawJsonMapper;
import ru.itmo.se.is.feature.fileimport.infrastructure.parser.json.raw.RawJsonMovie;
import ru.itmo.se.is.feature.movie.api.dto.MovieRequestDto;
import ru.itmo.se.is.shared.exception.FileImportException;

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
        try {
            RawJsonMovie[] arr = jsonb.fromJson(inputStream, RawJsonMovie[].class);
            return rawJsonMapper.toMovieRequestDto(Arrays.asList(arr));
        } catch (JsonbException | JsonParsingException e) {
            throw new FileImportException("Error while parsing json file", e);
        }
    }

    @Override
    public boolean supports(FileExtension fileExtension) {
        return FileExtension.JSON.equals(fileExtension);
    }
}
