package repository;

import enums.Location;
import model.DeliveryRoute;
import model.InMemoryData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RouteRepositoryImpl implements RouteRepository{

    private final InMemoryDataStore dataStore;

    public RouteRepositoryImpl(InMemoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void save(DeliveryRoute route) {
        InMemoryData data = dataStore.getData();
        data.getRoutes().removeIf(r -> r.getId() == route.getId());
        data.getRoutes().add(route);
    }

    @Override
    public Optional<DeliveryRoute> findById(int id) {
        return dataStore.getData().getRoutes().stream()
                .filter(r -> r.getId() == id)
                .findFirst();
    }

    @Override
    public List<DeliveryRoute> findAll() {
        return dataStore.getData().getRoutes();
    }

    @Override
    public List<DeliveryRoute> findByLocations(Location start, Location end) {
        return dataStore.getData().getRoutes().stream()
                .filter(r -> r.servesLocations(start, end))
                .collect(Collectors.toList());
    }

    @Override
    public int generateId() {
        return dataStore.getData().generateRouteId();
    }
}
