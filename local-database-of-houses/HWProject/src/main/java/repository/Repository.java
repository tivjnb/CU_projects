package repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll() throws Exception;
    void add(T entity) throws Exception;
    void update(int index, T entity) throws Exception;
    void delete(int index) throws Exception;
}