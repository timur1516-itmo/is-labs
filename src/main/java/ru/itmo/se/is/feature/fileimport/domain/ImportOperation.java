package ru.itmo.se.is.feature.fileimport.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.fileimport.domain.value.ImportStatus;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor

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
}
