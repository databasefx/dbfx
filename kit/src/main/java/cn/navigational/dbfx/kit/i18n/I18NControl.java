package cn.navigational.dbfx.kit.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class I18NControl extends ResourceBundle.Control {
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) {
        var bundleName = toBundleName(baseName, locale);
        var resourceName = toResourceName(bundleName, "properties");
        try (var is = loader.getResourceAsStream(resourceName)) {
            try (var isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                 var reader = new BufferedReader(isr)) {
                return new PropertyResourceBundle(reader);
            }
        } catch (IOException ex) {
            return null;
        }
    }
}
