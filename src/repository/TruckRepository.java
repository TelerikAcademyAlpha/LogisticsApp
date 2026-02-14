package repository;

import model.Truck;

import java.util.List;
import java.util.Optional;

public interface TruckRepository {
    void saveAll(List<Truck> trucks);

    Optional<Truck> findById(int id);

    List<Truck> findAll();

    List<Truck> findFree();

    List<Truck> findFreeWithCapacityAndRange(int requiredCapacityKg, int requiredRangeKm);
}
