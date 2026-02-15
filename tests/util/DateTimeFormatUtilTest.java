package util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeFormatUtilTest {

    @Test
    void parseValid() {
        LocalDateTime dt = DateTimeFormatterUtil.parse("2024-10-08 06:00");
        assertAll(
                () -> assertEquals(2024, dt.getYear()), () -> assertEquals(10, dt.getMonthValue()),
                () -> assertEquals(8, dt.getDayOfMonth()), () -> assertEquals(6, dt.getHour())
        );
    }

    @Test
    void parseTrimsWhitespace() { assertEquals(2024, DateTimeFormatterUtil.parse("  2024-10-08 06:00  ").getYear()); }

    @Test
    void formatValid() { assertEquals("2024-10-08 06:00", DateTimeFormatterUtil.format(LocalDateTime.of(2024, 10, 8, 6, 0))); }

    @Test
    void formatNull() { assertEquals("N/A", DateTimeFormatterUtil.format(null)); }

    @Test
    void parseInvalid() { assertThrows(IllegalArgumentException.class, () -> DateTimeFormatterUtil.parse("not-a-date")); }

    @Test
    void parseEmpty() { assertThrows(IllegalArgumentException.class, () -> DateTimeFormatterUtil.parse("")); }

    @Test
    void parseNull() { assertThrows(IllegalArgumentException.class, () -> DateTimeFormatterUtil.parse(null)); }

    @Test
    void roundTrip() {
        LocalDateTime original = LocalDateTime.of(2024, 12, 25, 14, 30);
        assertEquals(original, DateTimeFormatterUtil.parse(DateTimeFormatterUtil.format(original)));
    }
}
