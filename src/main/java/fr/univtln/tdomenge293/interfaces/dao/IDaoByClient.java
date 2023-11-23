package fr.univtln.tdomenge293.interfaces.dao;

import fr.univtln.tdomenge293.interfaces.IClient;
import fr.univtln.tdomenge293.utils.exceptions.DataAccessException;

public interface IDaoByClient<E, R extends IClient> {
    default E findByClient(R ref) throws DataAccessException { return findByClient(ref.getEmail()); }
    E findByClient(String email) throws DataAccessException;
    void removeByClient(R ref) throws DataAccessException;
}
