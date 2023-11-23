package fr.univ.tln.tdomenge293.utils.exceptions;

import fr.univ.tln.tdomenge293.utils.localization.LocalizedMsg;

public class DataAccessException extends RuntimeException {

    private static final String DATA_ACCESS_DB_DAO = "data.access.db_dao";

    /**
     * Constructeur d'une exception pour utiliser un message traduit à partir de son identifiant
     * @param msgKey l'identifiant pour le message de l'exception
     */
    public DataAccessException(String msgKey) { super( LocalizedMsg.getExceptionString(msgKey) ); }
    public DataAccessException(String msgKey, Throwable err) { super( LocalizedMsg.getExceptionString(msgKey), err); }
    
    /**
     * Constructeur d'une exception pour utiliser un message traduit à partir de son identifiant
     * Remplaçant les valeurs args dans le message final (utilisant MessageFormat)
     * @param msgKey l'identifiant pour le message de l'exception 
     * @param args les objets dans l'ordre à remplacer dans le message final
     */
    public DataAccessException(String msgKey, Object... args) { super( LocalizedMsg.getExceptionString(msgKey, args)); }
    public DataAccessException(String msgKey, Throwable err, Object... args) { super( LocalizedMsg.getExceptionString(msgKey, args), err); }

    public DataAccessException(String daoName, String className, String errMessage) { this(DATA_ACCESS_DB_DAO, daoName, className, errMessage); }
}
