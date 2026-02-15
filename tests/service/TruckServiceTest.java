package service;

import enums.TruckModel;
import model.InMemoryData;
import model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryDataStoreImpl;
import repository.TruckRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TruckServiceTest {

    private TruckService service;
    private InMemoryDataStoreImpl dataStore;

    @BeforeEach
    void setUp() {
        dataStore = InMemoryDataStoreImpl.getInstance();
        dataStore.setData(new InMemoryData());
        service = new TruckService(new TruckRepositoryImpl(dataStore));
    }

    @Test
    void initializeFleetCreatesCorrectNumberOfTrucks() {
        service.initializeFleet();

        List<Truck> trucks = service.findAll();
        assertEquals(40, trucks.size(), "Should create 40 trucks total");
    }

    @Test
    void initializeFleetCreatesCorrectTruckModels() {
        service.initializeFleet();

        List<Truck> trucks = service.findAll();

        long scaniaCount = trucks.stream().filter(t -> t.getModel() == TruckModel.SCANIA).count();
        long manCount = trucks.stream().filter(t -> t.getModel() == TruckModel.MAN).count();
        long actrosCount = trucks.stream().filter(t -> t.getModel() == TruckModel.ACTROS).count();

        assertAll(
                () -> assertEquals(10, scaniaCount, "Should have 10 SCANIA trucks (1001-1010)"),
                () -> assertEquals(15, manCount, "Should have 15 MAN trucks (1011-1025)"),
                () -> assertEquals(15, actrosCount, "Should have 15 ACTROS trucks (1026-1040)")
        );
    }

    @Test
    void initializeFleetCreatesCorrectSpecifications() {
        service.initializeFleet();

        // Check SCANIA specifications
        Optional<Truck> scania = service.findById(1001);
        assertTrue(scania.isPresent());
        assertAll(
                () -> assertEquals(TruckModel.SCANIA, scania.get().getModel()),
                () -> assertEquals(42000, scania.get().getCapacityKg()),
                () -> assertEquals(8000, scania.get().getMaxRangeKm()),
                () -> assertFalse(scania.get().isAssigned())
        );

        // Check MAN specifications
        Optional<Truck> man = service.findById(1011);
        assertTrue(man.isPresent());
        assertAll(
                () -> assertEquals(TruckModel.MAN, man.get().getModel()),
                () -> assertEquals(37000, man.get().getCapacityKg()),
                () -> assertEquals(10000, man.get().getMaxRangeKm()),
                () -> assertFalse(man.get().isAssigned())
        );

        // Check ACTROS specifications
        Optional<Truck> actros = service.findById(1026);
        assertTrue(actros.isPresent());
        assertAll(
                () -> assertEquals(TruckModel.ACTROS, actros.get().getModel()),
                () -> assertEquals(26000, actros.get().getCapacityKg()),
                () -> assertEquals(13000, actros.get().getMaxRangeKm()),
                () -> assertFalse(actros.get().isAssigned())
        );
    }

    @Test
    void initializeFleetDoesNotDuplicateIfAlreadyInitialized() {
        service.initializeFleet();
        assertEquals(40, service.findAll().size());

        service.initializeFleet();
        assertEquals(40, service.findAll().size(), "Should not duplicate trucks");
    }

    @Test
    void findByIdReturnsExistingTruck() {
        service.initializeFleet();

        Optional<Truck> truck = service.findById(1001);
        assertTrue(truck.isPresent());
        assertEquals(1001, truck.get().getId());
    }

    @Test
    void findByIdReturnsEmptyForNonExistent() {
        service.initializeFleet();

        Optional<Truck> truck = service.findById(9999);
        assertFalse(truck.isPresent());
    }

    @Test
    void findAllReturnsAllTrucks() {
        service.initializeFleet();

        List<Truck> trucks = service.findAll();
        assertEquals(40, trucks.size());
    }

    @Test
    void findAllReturnsEmptyListWhenNoTrucks() {
        List<Truck> trucks = service.findAll();
        assertNotNull(trucks);
        assertEquals(0, trucks.size());
    }

    @Test
    void findFreeReturnsAllWhenNoneAssigned() {
        service.initializeFleet();

        List<Truck> freeTrucks = service.findFree();
        assertEquals(40, freeTrucks.size());
    }

}
