package ru.itmo.se.is.shared.storage;

import ru.itmo.se.is.platform.storage.StoredFile;

import java.io.InputStream;

public interface FileStorage {

    StoredFile save(String fileName, InputStream content);

    InputStream get(String bucket, String objectKey);
}
