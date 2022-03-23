package com.kyancey.scheduling.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Utility class for Date Time Conversions between time zones.
 */
public class DateTimeConversion {
    /**
     * Converts UTC to Local Time
     * @param d Local time
     * @return
     */
    public static ZonedDateTime toLocalFromUTC(LocalDateTime d) {
        return d.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault());
    }

    /**
     * Converts Local Time to UTC
     * @param d UTC time
     * @return
     */
    public static ZonedDateTime toUTCFromLocal(LocalDateTime d) {
        ZonedDateTime z = ZonedDateTime.of(d, ZoneId.systemDefault());
        z = z.withZoneSameInstant(ZoneId.of("UTC"));
        return z;
    }

    /**
     * Converts Local Time to EST
     * @param d Local time
     * @return
     */
    public static ZonedDateTime toESTFromLocal(LocalDateTime d) {
        ZonedDateTime z = ZonedDateTime.of(d, ZoneId.systemDefault());
        z = z.withZoneSameInstant(ZoneId.of("EST5EDT"));
        return z;
    }

    /**
     * Formats Local Date Time as String
     * @param d Local date time
     * @return
     */
    public static String formatAsLocalDateTime(LocalDateTime d) {
        return toLocalFromUTC(d).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG));
    }
}
