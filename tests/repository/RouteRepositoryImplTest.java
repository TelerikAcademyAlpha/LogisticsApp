package repository;

import enums.Location;
import model.DeliveryRoute;
import model.InMemoryData;
import model.RouteStop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteRepositoryImplTest {
    private InMemoryDataStoreImpl dataStore;
    private RouteRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        dataStore = InMemoryDataStoreImpl.getInstance();
        dataStore.setData(new InMemoryData());
        repo = new RouteRepositoryImpl(dataStore);
    }

    private DeliveryRoute route(int id, Location... locs) {
        LocalDateTime t = LocalDateTime.of(2024, 10, 10, 6, 0);
        List<RouteStop> stops = new ArrayList<>();
        stops.add(new RouteStop(locs[0], t, true));
        for (int i = 1; i < locs.length; i++) {
            t = t.plusHours(10);
            stops.add(new RouteStop(locs[i], t, false));
        }
        return new DeliveryRoute(id, stops, 1000);
    }

    @Test
    void saveAndFind() {
        repo.save(route(1, Location.SYD, Location.MEL));
        assertTrue(repo.findById(1).isPresent());
        assertEquals(1, repo.findById(1).get().getId());
    }

    @Test
    void findNotFound() {
        assertFalse(repo.findById(999).isPresent());
    }

    @Test
    void findAll() {
        repo.save(route(1, Location.SYD, Location.MEL));
        repo.save(route(2, Location.BRI, Location.SYD));
        assertEquals(2, repo.findAll().size());
    }

    @Test
    void findByLocationsForward() {
        repo.save(route(1, Location.BRI, Location.SYD, Location.MEL));
        repo.save(route(2, Location.SYD, Location.MEL, Location.ADL));
        repo.save(route(3, Location.PER, Location.DAR));
        assertEquals(2, repo.findByLocations(Location.SYD, Location.MEL).size());
        assertEquals(1, repo.findByLocations(Location.BRI, Location.MEL).size());
    }

    @Test
    void findByLocationsReverseReturnsEmpty() {
        repo.save(route(1, Location.SYD, Location.MEL));
        assertEquals(0, repo.findByLocations(Location.MEL, Location.SYD).size());
    }

    @Test
    void findByLocationsNoMatch() {
        repo.save(route(1, Location.SYD, Location.MEL));
        assertEquals(0, repo.findByLocations(Location.PER, Location.DAR).size());
    }

    @Test
    void generateId() {
        assertEquals(1, repo.generateId());
        assertEquals(2, repo.generateId());
    }
}
