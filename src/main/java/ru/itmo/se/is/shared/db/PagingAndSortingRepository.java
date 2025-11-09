package ru.itmo.se.is.shared.db;

import java.util.List;
import java.util.Map;

public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {
    long count(Map<String, Object> filterBy);

    List<T> load(int first, int pageSize, String sortField, int sortOrder, Map<String, Object> filterBy);
}
