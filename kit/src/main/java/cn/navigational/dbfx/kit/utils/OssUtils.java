package cn.navigational.dbfx.kit.utils;

/**
 * Oss relative operation utils
 *
 * @author yangkui
 * @since 1.0
 */
public class OssUtils {
    /**
     * Get current user home
     *
     * @return User home dir
     */
    public static String getUserHome() {
        return System.getProperty("user.home");
    }
}
