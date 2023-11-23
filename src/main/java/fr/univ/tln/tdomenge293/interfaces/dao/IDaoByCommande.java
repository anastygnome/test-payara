package fr.univ.tln.tdomenge293.interfaces.dao;

import fr.univ.tln.tdomenge293.interfaces.ICommande;
import fr.univ.tln.tdomenge293.utils.exceptions.DataAccessException;

public interface IDaoByCommande<E, R extends ICommande> {
    default E findByCommande(R ref) throws DataAccessException { return findByCommande( ref.getNumero() ); }

    E findByCommande(Long id) throws DataAccessException;
    E updateByCommande(R ref) throws DataAccessException;
    void removeByCommande(R ref) throws DataAccessException;
}
