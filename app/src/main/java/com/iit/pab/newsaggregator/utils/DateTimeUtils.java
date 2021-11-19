package com.iit.pab.newsaggregator.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class DateTimeUtils {

    public static LocalDateTime parseDate(String date) {
        if (date != null) {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    // date/time
                    .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    // offset (hh:mm - "+00:00" when it's zero)
                    .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
                    // offset (hhmm - "+0000" when it's zero)
                    .optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
                    // offset (hh - "Z" when it's zero)
                    .optionalStart().appendOffset("+HH", "Z").optionalEnd()
                    // create formatter
                    .toFormatter();
            return !date.equals("null") ? LocalDateTime.parse(date, formatter) : null;
        }

        return null;
    }

    public static String formatDateTime(LocalDateTime ldt) {
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm", Locale.getDefault());
        return ldt.format(dtf);
    }
}
