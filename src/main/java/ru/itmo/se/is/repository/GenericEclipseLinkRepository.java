package ru.itmo.se.is.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.DeleteAllQuery;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.UnitOfWork;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unchecked")
public abstract class GenericEclipseLinkRepository<T, ID> implements Repository<T, ID> {

    private Class<T> entityClass;
    private DatabaseSession session;

    @Override
    public List<T> findAll() {
        ReadAllQuery query = new ReadAllQuery(entityClass);
        return (List<T>) session.executeQuery(query);
    }

    @Override
    public Optional<T> findById(ID id) {
        ReadObjectQuery query = new ReadObjectQuery(entityClass);
        query.setSelectionCriteria(new ExpressionBuilder().get("id").equal(id));
        T entity = (T) session.executeQuery(query);
        return Optional.ofNullable(entity);
    }

    @Override
    public T save(T entity) {
        session.insertObject(entity);
        return entity;
    }

    public void update(T entity, Consumer<T> fieldUpdater) {
        UnitOfWork uow = session.getActiveUnitOfWork();
        T managed = (T) uow.deepMergeClone(entity);
        fieldUpdater.accept(managed);
        registerNestedFields(uow, managed);
    }

    @Override
    public void deleteById(ID id) {
        UnitOfWork uow = session.getActiveUnitOfWork();
        DeleteAllQuery query = new DeleteAllQuery(entityClass);
        query.setSelectionCriteria(new ExpressionBuilder().get("id").equal(id));
        uow.executeQuery(query);
    }

    protected abstract void registerNestedFields(UnitOfWork uow, T entity);
}
