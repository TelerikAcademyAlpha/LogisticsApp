package model;

import enums.Location;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RouteStopTest {

    private final LocalDateTime time = LocalDateTime.of(2024, 10, 10, 6, 0);

    @Test
    void creation() { RouteStop s = new RouteStop(Location.SYD, time, true); assertAll(() -> assertEquals(Location.SYD, s.getLocation()), () -> assertEquals(time, s.getTime()), () -> assertTrue(s.isDeparture())); }

    @Test
    void
    defaultNotDeparture() { assertFalse(new RouteStop(Location.MEL, time).isDeparture()); }

    @Test
    void toStringDepart() { assertTrue(new RouteStop(Location.SYD, time, true).toString().contains("Depart")); }

    @Test
    void toStringArrive() { assertTrue(new RouteStop(Location.MEL, time, false).toString().contains("Arrive")); }
}
