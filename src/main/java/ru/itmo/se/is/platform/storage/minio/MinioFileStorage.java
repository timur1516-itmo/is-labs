package ru.itmo.se.is.platform.storage.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.itmo.se.is.platform.storage.StoredFile;
import ru.itmo.se.is.shared.storage.FileStorage;

import java.io.InputStream;
import java.util.UUID;

@ApplicationScoped
public class MinioFileStorage implements FileStorage {
    @Inject
    private MinioClient minioClient;

    String importsBucket = "imports";

    @Override
    public StoredFile save(String fileName, InputStream content) {
        try {
            String objectKey = UUID.randomUUID() + "_" + fileName;

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(importsBucket)
                    .object(objectKey)
                    .stream(content, -1, 10 * 1024 * 1024)
                    .contentType("application/octet-stream")
                    .build();

            minioClient.putObject(putObjectArgs);
            return new StoredFile(importsBucket, objectKey, fileName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to store file in MinIO", e);
        }
    }

    @Override
    public InputStream get(String bucket, String objectKey) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build()
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get file from MinIO", e);
        }
    }
}
