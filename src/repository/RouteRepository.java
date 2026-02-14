package repository;

import enums.Location;
import model.DeliveryRoute;

import java.util.List;
import java.util.Optional;

public interface RouteRepository {

    void save(DeliveryRoute route);

    Optional<DeliveryRoute> findById(int id);

    List<DeliveryRoute> findAll();

    List<DeliveryRoute> findByLocations(Location start, Location end);

    int generateId();

}
