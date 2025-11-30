package ru.itmo.se.is.platform.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoredFile {
    private String bucket;
    private String objectKey;
    private String originalFileName;
}
