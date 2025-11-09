package ru.itmo.se.is.feature.fileimport.infrastructure.db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperation;
import ru.itmo.se.is.feature.fileimport.domain.ImportOperationRepository;
import ru.itmo.se.is.platform.db.eclipselink.EclipseLinkPagingAndSortingRepository;
import ru.itmo.se.is.platform.db.eclipselink.UnitOfWorkManager;

@ApplicationScoped
@NoArgsConstructor
public class EclipseLinkImportOperationRepository
        extends EclipseLinkPagingAndSortingRepository<ImportOperation, Long>
        implements ImportOperationRepository {
    @Inject
    public EclipseLinkImportOperationRepository(UnitOfWorkManager unitOfWorkManager) {
        super(ImportOperation.class, unitOfWorkManager);
    }
}
