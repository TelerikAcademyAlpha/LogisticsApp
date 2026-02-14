package repository;

import enums.Location;
import model.DeliveryRoute;

import java.util.List;
import java.util.Optional;

public class RouteServiceImpl implements RouteRepository{
    @Override
    public void save(DeliveryRoute route) {

    }

    @Override
    public Optional<DeliveryRoute> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<DeliveryRoute> findAll() {
        return List.of();
    }

    @Override
    public List<DeliveryRoute> findByLocations(Location start, Location end) {
        return List.of();
    }

    @Override
    public int generateId() {
        return 0;
    }
}
