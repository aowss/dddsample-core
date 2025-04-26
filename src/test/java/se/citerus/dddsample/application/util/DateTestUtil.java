package se.citerus.dddsample.application.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * A utility class for handling dates in tests.
 */
public class DateTestUtil {
    
    public static Date toDate(int year, int month, int day) {
        return Date.from(
            LocalDate.of(year, month, day)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
        );
    }
    
    public static Instant toInstant(int year, int month, int day) {
        return LocalDate.of(year, month, day)
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC);
    }
} 