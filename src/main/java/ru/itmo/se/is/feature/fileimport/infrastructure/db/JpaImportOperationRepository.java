package ru.itmo.se.is.feature.fileimport.infrastructure.db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperationRepository;
import ru.itmo.se.is.platform.db.jpa.JpaPagingAndSortingRepository;

@ApplicationScoped
@NoArgsConstructor
public class JpaImportOperationRepository
        extends JpaPagingAndSortingRepository<ImportOperation, Long>
        implements ImportOperationRepository {
    @Inject
    public JpaImportOperationRepository(EntityManager entityManager) {
        super(ImportOperation.class, entityManager);
    }
}
