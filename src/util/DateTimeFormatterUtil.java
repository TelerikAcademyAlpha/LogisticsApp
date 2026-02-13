package util;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private DateTimeFormatterUtil() {}

    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime parse(String inptu) {
        if (inptu == null || inptu.trim().isEmpty()){
            throw new IllegalArgumentException("Datetime can not be empty");
        }

        try {
            return LocalDateTime.parse(inptu.trim(), FORMATTER);
        } catch (DateTimeException dte) {
            throw new IllegalArgumentException("Invalid datetime format: " + inptu.trim() + ". Expected format is : yyyy-MM-dd HH:mm ex.: (2026-13-02 16:30)");
        }
    }
}
