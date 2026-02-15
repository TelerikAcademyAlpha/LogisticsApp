package model;

import enums.Location;
import enums.PackageStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DeliveryPackageTest {
    private DeliveryPackage pkg;

    @BeforeEach
    void setUp() {
        pkg = new DeliveryPackage(
                1, Location.SYD, Location.MEL, 45.0,
                new ContactInfo("A", "C", "1 Street")
        );
    }

    @Test
    void id() {
        assertEquals(1, pkg.getId());
    }

    @Test
    void startLocation() {
        assertEquals(Location.SYD, pkg.getStartLocation());
    }

    @Test
    void endLocation() {
        assertEquals(Location.MEL, pkg.getEndLocation());
    }

    @Test
    void weight() {
        assertEquals(45.0, pkg.getWeight(), 0.01);
    }

    @Test
    void contactNotNull() {
        assertNotNull(pkg.getContactInfo());
    }

    @Test
    void defaultStatusUnassigned() {
        assertEquals(PackageStatus.UNASSIGNED, pkg.getStatus());
    }

    @Test
    void defaultArrivalNull() {
        assertNull(pkg.getExpectedArrival());
    }

    @Test
    void setStatus() {
        pkg.setStatus(PackageStatus.ASSIGNED);
        assertEquals(PackageStatus.ASSIGNED, pkg.getStatus());
    }

    @Test
    void setExpectedArrival() {
        LocalDateTime t = LocalDateTime.of(2024, 10, 11, 18, 0);
        pkg.setExpectedArrival(t);
        assertEquals(t, pkg.getExpectedArrival());
    }

    @Test
    void setWeight() {
        pkg.setWeight(100);
        assertEquals(100.0, pkg.getWeight(), 0.01);
    }
}
