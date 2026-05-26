package helpers;

import java.util.Locale;
import java.util.ResourceBundle;

public class LangManager {

    private static Locale locale;
    private static ResourceBundle bundle;
    private static Runnable onChange;

    static {
        String lang = Locale.getDefault().getLanguage();
        locale = lang.equals("es") ? Locale.forLanguageTag("es-ES") : Locale.ENGLISH;
        bundle = ResourceBundle.getBundle("i18n.Bundle", locale);
    }

    public static String get(String key) {
        try { return bundle.getString(key); }
        catch (Exception e) { return key; }
    }

    public static String format(String key, String value) {
        return get(key).replace("{0}", value);
    }

    public static void setLocale(Locale l) {
        locale = l;
        bundle = ResourceBundle.getBundle("i18n.Bundle", l);
        if (onChange != null) onChange.run();
    }

    public static void setOnChange(Runnable r) { onChange = r; }

    public static Locale getLocale() { return locale; }
    public static boolean isEnglish() { return locale.getLanguage().equals("en"); }
}
