package ru.itmo.se.is.feature.fileimport.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.se.is.feature.fileimport.domain.value.FileExtension;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "file_resource")
public class FileResource {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_extension", nullable = false)
    @Enumerated(EnumType.STRING)
    private FileExtension fileExtension;

    @Column(name = "file_object_key", nullable = false)
    private String fileObjectKey;
}
