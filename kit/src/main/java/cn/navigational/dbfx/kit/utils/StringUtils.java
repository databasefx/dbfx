package cn.navigational.dbfx.kit.utils;

import java.util.UUID;

/**
 * String utils
 *
 * @author yangkui
 * @since 1.0
 */
public class StringUtils {
    /**
     * Target str is empty.
     *
     * @param str target str
     * @return str is empty
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * Target str string is not empty
     *
     * @param str Target str string
     * @return str is not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Generate a UUID string
     *
     * @return uuid string
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * If target is empty return defaultVal otherwise return target
     *
     * @param target     target str
     * @param defaultVal default value
     * @return str
     */
    public static String getValueIfEmpty(String target, String defaultVal) {
        return isEmpty(target) ? defaultVal : target;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            char x = hexArray[v >>> 4];
            char y = hexArray[v & 0x0F];
            sb.append(x).append(y);
            if (j!=bytes.length-1){
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
