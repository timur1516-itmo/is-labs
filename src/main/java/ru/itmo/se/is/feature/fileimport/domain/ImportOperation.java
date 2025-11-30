package ru.itmo.se.is.feature.fileimport.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "ImportOperation")
@Entity
@Table(name = "import_operation")
public class ImportOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImportStatus status;

    @Column(name = "start_dt", nullable = false)
    private ZonedDateTime startDt;

    @Column(name = "end_dt", nullable = false)
    private ZonedDateTime endDt;

    @Column(name = "imported_cnt")
    private Long importedCnt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_bucket", nullable = false)
    private String fileBucket;

    @Column(name = "file_object_key", nullable = false)
    private String fileObjectKey;
}
