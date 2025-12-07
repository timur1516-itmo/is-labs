package ru.itmo.se.is.platform.storage.minio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Getter
@ApplicationScoped
public class MinioConfig {

    @Inject
    @ConfigProperty(name = "minio.url", defaultValue = "http://localhost:9000")
    private String minioUrl;

    @Inject
    @ConfigProperty(name = "minio.accessKey", defaultValue = "minioadmin")
    private String accessKey;

    @Inject
    @ConfigProperty(name = "minio.secretKey", defaultValue = "minioadmin")
    private String secretKey;
}
