package cn.navigational.dbfx.i18n;


import java.text.MessageFormat;
import java.util.ResourceBundle;

public class I18N {

    private static ResourceBundle bundle;

    private static final String PACKAGE_NAME = "i18n";

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
            bundle = ResourceBundle.getBundle(PACKAGE_NAME + ".dbfx", UTF_ENCODING_CONTROL);
        }
        return bundle;
    }
}
