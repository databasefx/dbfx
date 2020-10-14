package cn.navigational.dbfx.preferences;

/**
 * Defines preferences for dbfx App.
 *
 * @author yangkui
 * @since 1.0
 */
public class PreferencesController {
    private static PreferencesController singleton;


    private PreferencesController() {

    }

    public static synchronized PreferencesController getSingleton() {
        if (singleton == null) {
            singleton = new PreferencesController();
        }
        return singleton;
    }
}
