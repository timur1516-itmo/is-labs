package ru.itmo.se.is.platform.storage.minio;

import io.minio.MinioClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class MinioClientProducer {

    @Inject
    MinioConfig config;

    @Produces
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(config.getMinioUrl())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
    }
}
