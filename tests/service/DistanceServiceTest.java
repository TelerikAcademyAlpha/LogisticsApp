package service;

import enums.Location;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceServiceTest {
    private final DistanceService ds = new DistanceService();

    @Test
    void sydToMel() {
        assertEquals(877, ds.getDistance(Location.SYD, Location.MEL));
    }

    @Test
    void symmetric() {
        assertEquals(ds.getDistance(Location.SYD, Location.MEL), ds.getDistance(Location.MEL, Location.SYD));
        assertEquals(ds.getDistance(Location.ASP, Location.DAR), ds.getDistance(Location.DAR, Location.ASP));
    }

    @Test
    void sameCityZero() {
        assertEquals(0, ds.getDistance(Location.SYD, Location.SYD));
    }

    @Test
    void specificDistances() {
        assertAll(
                () -> assertEquals(1376, ds.getDistance(Location.SYD, Location.ADL)),
                () -> assertEquals(1497, ds.getDistance(Location.ASP, Location.DAR)),
                () -> assertEquals(4311, ds.getDistance(Location.BRI, Location.PER)),
                () -> assertEquals(4025, ds.getDistance(Location.DAR, Location.PER))
        );
    }

    @Test
    void totalRouteDistance() {
        assertEquals(1602, ds.calculateTotalDistance(
                Arrays.asList(Location.SYD, Location.MEL, Location.ADL)));
    }

}
