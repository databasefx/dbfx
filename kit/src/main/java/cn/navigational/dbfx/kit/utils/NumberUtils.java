package cn.navigational.dbfx.kit.utils;

import java.util.regex.Pattern;

/**
 * Number utils
 *
 * @author yangkui
 * @since 1.0
 */
public class NumberUtils {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");

    /**
     * Give str is number?
     *
     * @param str target str
     * @return true or false
     */
    public static boolean isInteger(String str) {
        return NUMBER_PATTERN.matcher(str).matches();
    }
}
