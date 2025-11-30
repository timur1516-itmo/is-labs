package ru.itmo.se.is.platform.storage.minio;

import io.minio.MinioClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class MinioClientProducer {
    String minioUrl = "http://localhost:9000";

    String accessKey = "minioadmin";

    String secretKey = "minioadmin";

    @Produces
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }
}
