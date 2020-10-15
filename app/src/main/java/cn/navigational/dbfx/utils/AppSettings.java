package cn.navigational.dbfx.utils;

import cn.navigational.dbfx.model.Manifest;
import cn.navigational.dbfx.model.TableSetting;

/**
 * dbfx app setting
 *
 * @author yangkui
 * @since 1.0
 */
public class AppSettings {
    /**
     * MANIFEST.MF file
     */
    private Manifest manifest;
    /**
     * Application table data setting
     */
    private TableSetting tableSetting;

    private static AppSettings appSettings = new AppSettings();

    public TableSetting getTableSetting() {
        return tableSetting;
    }

    public void setTableSetting(TableSetting tableSetting) {
        this.tableSetting = tableSetting;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    public static AppSettings getAppSettings() {
        return appSettings;
    }

    public static synchronized void setAppSettings(AppSettings settings) {
        AppSettings.appSettings = settings;
    }
}
