package ru.itmo.se.is.feature.fileimport.infrastructure.parser;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import ru.itmo.se.is.feature.fileimport.domain.value.FileExtension;
import ru.itmo.se.is.shared.exception.FileImportException;

@ApplicationScoped
public class ImportFileParserFactory {

    @Inject
    private Instance<MovieImportFileParser> parsers;

    public MovieImportFileParser getParser(FileExtension format) {
        return parsers.stream()
                .filter(parser -> parser.supports(format))
                .findFirst()
                .orElseThrow(() -> new FileImportException("Unsupported format: " + format));
    }
}
