package fr.univtln.tdomenge293.interfaces.dao;

import java.util.List;

import fr.univtln.tdomenge293.utils.exceptions.DataAccessException;
import fr.univtln.tdomenge293.utils.pagination.Page;

public interface IDao<E, T> {
    E find(T id) throws DataAccessException;
    
    default Page<E> findAll() throws DataAccessException { return findAll(1, 100); }
    Page<E> findAll(int pageNumber, int pageSize) throws DataAccessException;
    
    E persist(E entity) throws DataAccessException;
    default void persistAll(List<E> entities) throws DataAccessException { entities.forEach(this::persist); }
    
    E update(E entity) throws DataAccessException;
    
    void remove(E entity) throws DataAccessException;
    void removeById(T id) throws DataAccessException;
}