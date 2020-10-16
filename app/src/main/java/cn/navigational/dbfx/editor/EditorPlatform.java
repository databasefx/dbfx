package cn.navigational.dbfx.editor;

import cn.navigational.dbfx.kit.utils.OssUtils;

import java.io.File;
import java.util.Locale;

public class EditorPlatform {
    /**
     * Application name
     */
    public static final String APP_NAME = "dbfx";
    /**
     * Current os name
     */
    private static final String OS_NAME = OssUtils.getOsName().toLowerCase(Locale.ROOT);
    /**
     * True if current platform is running Linux.
     */
    public static final boolean IS_LINUX = OS_NAME.contains("linux");

    /**
     * True if current platform is running Mac OS X.
     */
    public static final boolean IS_MAC = OS_NAME.contains("mac");

    /**
     * True if current platform is running Windows.
     */
    public static final boolean IS_WINDOWS = OS_NAME.contains("windows");
    /**
     * Application log file direction
     */
    public static final String APP_LOG_PATH = OssUtils.getUserHome() + File.separator + APP_NAME + File.separator + "logs" + File.separator;

    /**
     * Returns true if the jvm is running with assertions enabled.
     *
     * @return true if the jvm is running with assertions enabled.
     */
    public static boolean isAssertionEnabled() {
        return EditorPlatform.class.desiredAssertionStatus();
    }
}
