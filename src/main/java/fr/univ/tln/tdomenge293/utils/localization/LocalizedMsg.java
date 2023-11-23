package fr.univ.tln.tdomenge293.utils.localization;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizedMsg {
    private static Locale localDefault = Locale.forLanguageTag("fr");

    private static ResourceBundle exceptionBundle = ResourceBundle.getBundle("localization.exceptions.Exception", localDefault);
    private static ResourceBundle generalBundle = ResourceBundle.getBundle("localization.general.General", localDefault);

    private LocalizedMsg() {}

    public static String getString(ResourceBundle bundle, String key) { return bundle.getString(key); }
    public static String getString(ResourceBundle bundle, String key, Object... args) { return MessageFormat.format(bundle.getString(key), args); }

    public static String getExceptionString(String key, Object... args) { return getString(exceptionBundle, key, args); }
    public static String getGeneralString(String key, Object... args) { return getString(generalBundle, key, args); }
}
