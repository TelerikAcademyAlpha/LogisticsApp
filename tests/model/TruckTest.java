package model;

import enums.TruckModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TruckTest {

    @Test
    void constructorInitializesAllFields() {
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);

        assertAll(
                () -> assertEquals(1001, truck.getId()),
                () -> assertEquals(TruckModel.SCANIA, truck.getModel()),
                () -> assertEquals(42000, truck.getCapacityKg()),
                () -> assertEquals(8000, truck.getMaxRangeKm()),
                () -> assertFalse(truck.isAssigned(), "Truck should not be assigned by default")
        );
    }

    @Test
    void setAndGetModel() {
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        truck.setModel(TruckModel.MAN);
        assertEquals(TruckModel.MAN, truck.getModel());
    }

    @Test
    void setAndGetCapacityKg() {
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        truck.setCapacityKg(50000);
        assertEquals(50000, truck.getCapacityKg());
    }

    @Test
    void setAndGetMaxRangeKm() {
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        truck.setMaxRangeKm(10000);
        assertEquals(10000, truck.getMaxRangeKm());
    }

    @Test
    void setAndGetAssigned() {
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        assertFalse(truck.isAssigned());

        truck.setAssigned(true);
        assertTrue(truck.isAssigned());

        truck.setAssigned(false);
        assertFalse(truck.isAssigned());
    }

    @Test
    void toStringContainsKeyInformation() {
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        String result = truck.toString();

        assertAll(
                () -> assertTrue(result.contains("1001"), "Should contain truck ID"),
                () -> assertTrue(result.contains("SCANIA"), "Should contain model"),
                () -> assertTrue(result.contains("42,000"), "Should contain capacity"),
                () -> assertTrue(result.contains("8,000"), "Should contain range"),
                () -> assertTrue(result.contains("FREE"), "Should contain assignment status")
        );
    }

    @Test
    void toStringShowsAssignedStatus() {
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        truck.setAssigned(true);
        String result = truck.toString();

        assertTrue(result.contains("ASSIGNED"), "Should show ASSIGNED when truck is assigned");
    }

    @Test
    void differentTruckModels() {
        Truck scania = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        Truck man = new Truck(1011, TruckModel.MAN, 37000, 10000);
        Truck actros = new Truck(1026, TruckModel.ACTROS, 26000, 13000);

        assertAll(
                () -> assertEquals(TruckModel.SCANIA, scania.getModel()),
                () -> assertEquals(TruckModel.MAN, man.getModel()),
                () -> assertEquals(TruckModel.ACTROS, actros.getModel())
        );
    }

    @Test
    void idIsImmutable() {
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        assertEquals(1001, truck.getId());
        // ID should remain the same - no setter exists for ID
        assertEquals(1001, truck.getId());
    }
}
