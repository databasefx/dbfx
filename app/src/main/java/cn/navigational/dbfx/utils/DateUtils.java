package cn.navigational.dbfx.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateUtils {
    /**
     * Format local time according by fix format
     *
     * @param localDateTime Target localDateTime
     * @param format        Format str
     * @return Format after local time str
     */
    public static String formatLocalTime(LocalDateTime localDateTime, String format) {
        Objects.requireNonNull(format);
        Objects.requireNonNull(localDateTime);
        var formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }
}
