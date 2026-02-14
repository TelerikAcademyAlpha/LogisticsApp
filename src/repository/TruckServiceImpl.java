package repository;

import model.Truck;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TruckServiceImpl implements TruckRepository {

    private final InMemoryDataStore dataStore;

    public TruckServiceImpl(InMemoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void saveAll(List<Truck> trucks) {
        dataStore.getData().setTrucks(trucks);
    }

    @Override
    public Optional<Truck> findById(int id) {
        return dataStore.getData().getTrucks().stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    @Override
    public List<Truck> findAll() {
        return dataStore.getData().getTrucks();
    }

    @Override
    public List<Truck> findFree() {
        return dataStore.getData().getTrucks().stream()
                .filter(t -> !t.isAssigned())
                .collect(Collectors.toList());
    }

    @Override
    public List<Truck> findFreeWithCapacityAndRange(int requiredCapacityKg, int requiredRangeKm) {
        return dataStore.getData().getTrucks().stream()
                .filter(t -> !t.isAssigned())
                .filter(t -> t.getCapacityKg() >= requiredCapacityKg)
                .filter(t -> t.getMaxRangeKm() >= requiredRangeKm)
                .collect(Collectors.toList());
    }
}
