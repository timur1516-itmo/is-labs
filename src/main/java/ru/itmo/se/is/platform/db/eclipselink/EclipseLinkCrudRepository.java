package ru.itmo.se.is.platform.db.eclipselink;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.sessions.UnitOfWork;
import ru.itmo.se.is.shared.db.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unchecked")
public abstract class EclipseLinkCrudRepository<T, ID> implements CrudRepository<T, ID> {

    protected Class<T> entityClass;
    protected UnitOfWorkManager unitOfWorkManager;

    @Override
    public List<T> findAll() {
        UnitOfWork uow = unitOfWorkManager.getCurrent();
        return (List<T>) uow.readAllObjects(entityClass);
    }

    @Override
    public Optional<T> findById(ID id) {
        UnitOfWork uow = unitOfWorkManager.getCurrent();
        ExpressionBuilder b = new ExpressionBuilder();
        T entity = (T) uow.readObject(
                entityClass,
                b.get("id").equal(id)
        );
        return Optional.ofNullable(entity);
    }

    @Override
    public T save(T entity) {
        UnitOfWork uow = unitOfWorkManager.getCurrent();
        System.out.println("UOW in save: " + System.identityHashCode(uow));
        if (uow == null) return null;
        return (T) uow.registerObject(entity);
    }

    public void update(T entity, Consumer<T> fieldUpdater) {
        UnitOfWork uow = unitOfWorkManager.getCurrent();
        T managed = (T) uow.readObject(entity);
        fieldUpdater.accept(managed);
        registerNestedFields(uow, managed);
    }

    @Override
    public void deleteById(ID id) {
        UnitOfWork uow = unitOfWorkManager.getCurrent();
        findById(id).ifPresent(uow::deleteObject);
    }

    protected void registerNestedFields(UnitOfWork uow, T entity) {
    }

    ;
}
