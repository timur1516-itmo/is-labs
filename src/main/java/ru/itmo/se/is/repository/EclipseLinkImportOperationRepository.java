package ru.itmo.se.is.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import ru.itmo.se.is.db.UnitOfWorkManager;
import ru.itmo.se.is.entity.ImportOperation;

@ApplicationScoped
@NoArgsConstructor
public class EclipseLinkImportOperationRepository extends GenericEclipseLinkRepository<ImportOperation, Long> {
    @Inject
    public EclipseLinkImportOperationRepository(UnitOfWorkManager unitOfWorkManager) {
        super(ImportOperation.class, unitOfWorkManager);
    }
}
