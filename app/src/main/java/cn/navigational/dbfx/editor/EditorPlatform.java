package cn.navigational.dbfx.editor;

import java.util.Locale;

public class EditorPlatform {
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ROOT);

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
     * Returns true if the jvm is running with assertions enabled.
     *
     * @return true if the jvm is running with assertions enabled.
     */
    public static boolean isAssertionEnabled() {
        return EditorPlatform.class.desiredAssertionStatus();
    }
}
