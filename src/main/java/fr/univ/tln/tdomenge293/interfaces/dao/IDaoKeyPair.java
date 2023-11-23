package fr.univ.tln.tdomenge293.interfaces.dao;

import fr.univ.tln.tdomenge293.utils.exceptions.DataAccessException;

public interface IDaoKeyPair<T, E> {
    E persist(T t, E e) throws DataAccessException;

    void remove(T t, E e) throws DataAccessException;
}
