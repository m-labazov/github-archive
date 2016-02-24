package ua.home.github.archive.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd-H");
    private static final DateTimeFormatter GITHUB_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd-H");

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date, DATE_FORMAT);
    }

    public static String format(LocalDateTime date) {
        return DATE_FORMAT.format(date);
    }

    public static Date parseGithubDate(String date) {
        return Date.from(
                    LocalDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        .atZone(ZoneId.systemDefault()).toInstant()
               );
    }

    public static Date toDate(LocalDateTime from) {
        return Date.from(
                from.atZone(ZoneId.systemDefault()).toInstant()
        );
    }
}
