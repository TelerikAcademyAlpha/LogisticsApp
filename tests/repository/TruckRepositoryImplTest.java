package repository;

import enums.TruckModel;
import model.InMemoryData;
import model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TruckRepositoryImplTest {
    private InMemoryDataStoreImpl dataStore;
    private TruckRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        dataStore = InMemoryDataStoreImpl.getInstance();
        dataStore.setData(new InMemoryData());
        repository = new TruckRepositoryImpl(dataStore);
    }

    private List<Truck> createTestFleet() {
        List<Truck> trucks = new ArrayList<>();
        trucks.add(new Truck(1001, TruckModel.SCANIA, 42000, 8000));
        trucks.add(new Truck(1002, TruckModel.SCANIA, 42000, 8000));
        trucks.add(new Truck(1011, TruckModel.MAN, 37000, 10000));
        trucks.add(new Truck(1026, TruckModel.ACTROS, 26000, 13000));
        return trucks;
    }

    @Test
    void saveAllAndFindAll() {
        List<Truck> trucks = createTestFleet();
        repository.saveAll(trucks);

        List<Truck> result = repository.findAll();
        assertEquals(4, result.size());
    }

    @Test
    void saveAllReplacesExistingTrucks() {
        List<Truck> trucks1 = createTestFleet();
        repository.saveAll(trucks1);
        assertEquals(4, repository.findAll().size());

        List<Truck> trucks2 = new ArrayList<>();
        trucks2.add(new Truck(2001, TruckModel.SCANIA, 42000, 8000));
        repository.saveAll(trucks2);

        assertEquals(1, repository.findAll().size());
        assertEquals(2001, repository.findAll().get(0).getId());
    }

    @Test
    void findByIdReturnsCorrectTruck() {
        repository.saveAll(createTestFleet());

        Optional<Truck> truck = repository.findById(1001);
        assertTrue(truck.isPresent());
        assertAll(
                () -> assertEquals(1001, truck.get().getId()),
                () -> assertEquals(TruckModel.SCANIA, truck.get().getModel()),
                () -> assertEquals(42000, truck.get().getCapacityKg()),
                () -> assertEquals(8000, truck.get().getMaxRangeKm())
        );
    }
}
