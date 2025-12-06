package ru.itmo.se.is.platform.db.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.itmo.se.is.platform.cache.infinispan.CacheLogged;
import ru.itmo.se.is.shared.db.CrudRepository;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public abstract class JpaCrudRepository<T, ID> implements CrudRepository<T, ID> {

    protected Class<T> entityClass;
    protected EntityManager em;

    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @CacheLogged
    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(
                em.find(entityClass, id)
        );
    }

    @Override
    public T save(T entity) {
        return em.merge(entity);
    }

    @Override
    public void deleteById(ID id) {
        em.remove(em.getReference(entityClass, id));
    }

    @Override
    public void delete(T entity) {
        em.remove(entity);
    }
}
