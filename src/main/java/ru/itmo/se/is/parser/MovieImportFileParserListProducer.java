package ru.itmo.se.is.parser;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MovieImportFileParserListProducer {
    @Inject
    private Instance<MovieImportFileParser> importFileParserInstances;

    @Produces
    @ApplicationScoped
    public List<MovieImportFileParser> produceParsersList() {
        return importFileParserInstances.stream().collect(Collectors.toList());
    }
}
