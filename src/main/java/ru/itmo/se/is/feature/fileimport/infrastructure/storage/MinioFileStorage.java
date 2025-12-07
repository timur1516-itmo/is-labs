package ru.itmo.se.is.feature.fileimport.infrastructure.storage;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.itmo.se.is.shared.storage.FileStorage;

import java.io.ByteArrayInputStream;

@ApplicationScoped
public class MinioFileStorage implements FileStorage {
    @Inject
    StorageConfig storageConfig;

    @Inject
    private MinioClient minioClient;

    @Override
    public void save(String objectKey, byte[] content) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(storageConfig.getImportsBucket())
                        .object(objectKey)
                        .stream(new ByteArrayInputStream(content), -1, 10 * 1024 * 1024)
                        .contentType("application/octet-stream")
                        .build()
        );
    }

    @Override
    public byte[] get(String objectKey) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(storageConfig.getImportsBucket())
                        .object(objectKey)
                        .build()
        ).readAllBytes();
    }

    @Override
    public void delete(String objectKey) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(storageConfig.getImportsBucket())
                        .object(objectKey)
                        .build()
        );
    }
}
