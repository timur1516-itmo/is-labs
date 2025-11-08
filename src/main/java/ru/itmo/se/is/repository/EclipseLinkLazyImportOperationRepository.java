package ru.itmo.se.is.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import ru.itmo.se.is.db.UnitOfWorkManager;
import ru.itmo.se.is.entity.ImportOperation;

@ApplicationScoped
@NoArgsConstructor
public class EclipseLinkLazyImportOperationRepository extends GenericEclipseLinkLazyRepository<ImportOperation> {
    @Inject
    public EclipseLinkLazyImportOperationRepository(UnitOfWorkManager unitOfWorkManager) {
        super(ImportOperation.class, unitOfWorkManager);
    }
}
