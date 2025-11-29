package ru.itmo.se.is.platform.db.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.NoArgsConstructor;
import ru.itmo.se.is.shared.db.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public abstract class JpaPagingAndSortingRepository<T, ID>
        extends JpaCrudRepository<T, ID>
        implements PagingAndSortingRepository<T, ID> {

    protected JpaPagingAndSortingRepository(Class<T> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }

    @Override
    public long count(Map<String, Object> filterBy) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(this.entityClass);
        cq = cq.select(cb.count(root));
        this.applyFilters(cb, cq, root, filterBy);
        TypedQuery<Long> query = em.createQuery(cq);
        return query.getSingleResult();
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, int sortOrder, Map<String, Object> filterBy) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(this.entityClass);
        Root<T> root = cq.from(this.entityClass);
        cq = cq.select(root);
        this.applyFilters(cb, cq, root, filterBy);
        this.applySort(cb, cq, root, sortField, sortOrder);
        TypedQuery<T> query = em.createQuery(cq);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    private void applySort(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, String sortField, int sortOrder) {
        if (sortField != null && !sortField.isEmpty()) {
            Path<?> sortPath = root.get(sortField);
            cq.orderBy(sortOrder == -1 ? cb.desc(sortPath) : cb.asc(sortPath));
        }
    }

    private void applyFilters(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<T> root, Map<String, Object> filterBy) {
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> entry : filterBy.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            predicates.add(cb.equal(root.get(key), value));
        }
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }
    }
}

