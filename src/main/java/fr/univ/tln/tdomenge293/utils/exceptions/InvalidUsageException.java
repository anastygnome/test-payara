package fr.univ.tln.tdomenge293.utils.exceptions;

import fr.univ.tln.tdomenge293.utils.localization.LocalizedMsg;

public class InvalidUsageException extends RuntimeException{

    private static final String INVALID_USAGE_KEY = "invalid.method.usage";

    public InvalidUsageException(String methodName, String className) { super( LocalizedMsg.getExceptionString(INVALID_USAGE_KEY, methodName, className) ); }
}
