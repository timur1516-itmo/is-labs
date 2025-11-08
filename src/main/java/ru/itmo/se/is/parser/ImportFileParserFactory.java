package ru.itmo.se.is.parser;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.itmo.se.is.dto.importing.ImportFileFormat;

import java.util.List;

@ApplicationScoped
public class ImportFileParserFactory {

    @Inject
    private List<MovieImportFileParser> parsers;

    public MovieImportFileParser getParser(ImportFileFormat format) {
        return parsers.stream()
                .filter(parser -> parser.supports(format))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported format: " + format));
    }
}
