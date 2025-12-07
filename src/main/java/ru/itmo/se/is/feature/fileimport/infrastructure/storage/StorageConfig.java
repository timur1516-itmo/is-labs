package ru.itmo.se.is.feature.fileimport.infrastructure.storage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Getter
@ApplicationScoped
public class StorageConfig {

    @Inject
    @ConfigProperty(name = "storage.importsBucket", defaultValue = "imports")
    private String importsBucket;
}
