package service;

import enums.TruckModel;
import model.Truck;
import repository.TruckRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TruckService {

    private final TruckRepository truckRepository;

    public TruckService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    /**
     * Initializes the truck fleet if not already present.
     */
    public void initializeFleet() {
        if (!truckRepository.findAll().isEmpty()) {
            return;
        }

        List<Truck> trucks = new ArrayList<>();

        // Scania: 1001-1010, 42000 kg, 8000 km
        for (int id = 1001; id <= 1010; id++) {
            trucks.add(new Truck(id, TruckModel.SCANIA, 42000, 8000));
        }
        // Man: 1011-1025, 37000 kg, 10000 km
        for (int id = 1011; id <= 1025; id++) {
            trucks.add(new Truck(id, TruckModel.MAN, 37000, 10000));
        }
        // Actros: 1026-1040, 26000 kg, 13000 km
        for (int id = 1026; id <= 1040; id++) {
            trucks.add(new Truck(id, TruckModel.ACTROS, 26000, 13000));
        }

        truckRepository.saveAll(trucks);
    }

    public Optional<Truck> findById(int id) {
        return truckRepository.findById(id);
    }

    public List<Truck> findAll() {
        return truckRepository.findAll();
    }

    public List<Truck> findFree() {
        return truckRepository.findFree();
    }

    public List<Truck> findSuitableForRoute(int requiredCapacityKg, int requiredRangeKm) {
        return truckRepository.findFreeWithCapacityAndRange(requiredCapacityKg, requiredRangeKm);
    }
}
