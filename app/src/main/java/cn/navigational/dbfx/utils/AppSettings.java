package cn.navigational.dbfx.utils;

import cn.navigational.dbfx.model.TableSetting;

/**
 * dbfx app setting
 *
 * @author yangkui
 * @since 1.0
 */
public class AppSettings {
    /**
     * Application table data setting
     */
    public TableSetting tableSetting;

    private static AppSettings appSettings = new AppSettings();

    public TableSetting getTableSetting() {
        return tableSetting;
    }

    public void setTableSetting(TableSetting tableSetting) {
        this.tableSetting = tableSetting;
    }

    public static synchronized AppSettings getAppSettings() {
        return appSettings;
    }

    public static synchronized void setAppSettings(AppSettings settings) {
        AppSettings.appSettings = settings;
    }
}
