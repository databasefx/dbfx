package cn.navigational.dbfx.i18n;


import java.text.MessageFormat;
import java.util.ResourceBundle;

public class I18N {

    private static ResourceBundle bundle;

    private static final ResourceBundle.Control UTF_ENCODING_CONTROL = new I18NControl();

    public static String getString(String key) {
        return getBundle().getString(key);
    }

    public static String getString(String key, Object... arguments) {
        final String pattern = getString(key);
        return MessageFormat.format(pattern, arguments);
    }

    public static synchronized ResourceBundle getBundle() {
        if (bundle == null) {
            final String packageName = "i18n/";
            //NOI18N
            bundle = ResourceBundle.getBundle(packageName + ".dbfx", UTF_ENCODING_CONTROL);
        }
        return bundle;
    }
}
