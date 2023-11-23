package fr.univ.tln.tdomenge293.interfaces.dao;

import fr.univ.tln.tdomenge293.interfaces.IProduit;
import fr.univ.tln.tdomenge293.utils.exceptions.DataAccessException;

public interface IDaoByProduit<E, R extends IProduit> {
    default E findByProduit(R ref) throws DataAccessException { return findByProduit(ref.getCode()); }
    E findByProduit(Long id) throws DataAccessException;
    E updateByProduit(R ref) throws DataAccessException;
    void removeByProduit(R ref) throws DataAccessException;
}
