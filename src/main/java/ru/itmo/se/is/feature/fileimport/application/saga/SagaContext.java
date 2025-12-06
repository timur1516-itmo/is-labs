package ru.itmo.se.is.feature.fileimport.application.saga;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.feature.fileimport.domain.value.FileExtension;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SagaContext {
    private String fileName;
    private FileExtension fileExtension;
    private byte[] content;

    private String objectKey;
    private long importedCount;

    private ImportOperation importOperation;
    private Exception error;

    private List<Long> savedObjectsIndexes = new ArrayList<>();
}
