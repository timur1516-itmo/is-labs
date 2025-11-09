package ru.itmo.se.is.feature.fileimport.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportStatus;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ImportOperation {
    private Long id;
    private ImportStatus status;
    private ZonedDateTime startDt;
    private ZonedDateTime endDt;
    private Long importedCnt;
    private String errorMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportOperation importOperation = (ImportOperation) o;
        return Objects.equals(id, importOperation.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }
}
