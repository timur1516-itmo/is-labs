package ru.itmo.se.is.shared.storage;

public interface FileStorage {

    void save(String objectKey, byte[] content) throws Exception;

    byte[] get(String objectKey) throws Exception;

    void delete(String objectKey) throws Exception;
}
