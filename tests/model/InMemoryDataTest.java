package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryDataTest {

    @Test
    void emptyOnCreation() {
        InMemoryData data = new InMemoryData();
        assertAll(
                () -> assertEquals(0, data.getPackages().size()),
                () -> assertEquals(0, data.getRoutes().size()),
                () -> assertEquals(0, data.getTrucks().size())
        );
    }

    @Test
    void initialIds() {
        InMemoryData data = new InMemoryData();
        assertAll(
                () -> assertEquals(1, data.getNextPackageId()),
                () -> assertEquals(1, data.getNextRouteId())
        );
    }

    @Test
    void generatePackageIdAutoIncrements() {
        InMemoryData data = new InMemoryData();
        assertEquals(1, data.generatePackageId());
        assertEquals(2, data.generatePackageId());
        assertEquals(3, data.generatePackageId());
    }

    @Test
    void generateRouteIdAutoIncrements() {
        InMemoryData data = new InMemoryData();
        assertEquals(1, data.generateRouteId());
        assertEquals(2, data.generateRouteId());
    }

    @Test
    void nextIdReflectsGeneration() {
        InMemoryData data = new InMemoryData();
        data.generatePackageId();
        data.generatePackageId();
        assertEquals(3, data.getNextPackageId());
    }
}
