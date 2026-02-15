package service;

import enums.Location;
import enums.PackageStatus;
import enums.TruckModel;
import exception.InvalidInputException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryDataStoreImpl;
import repository.RouteRepositoryImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RouteServiceTest {
    private RouteService service;
    private DistanceService distanceService;
    private TimeEstiomationService timeService;
    private InMemoryDataStoreImpl dataStore;

    @BeforeEach
    void setUp() {
        dataStore = InMemoryDataStoreImpl.getInstance();
        dataStore.setData(new InMemoryData());

        distanceService = new DistanceService();
        timeService = new TimeEstiomationService(distanceService);
        service = new RouteService(new RouteRepositoryImpl(dataStore), distanceService, timeService);
    }

    private ContactInfo testContact() {
        return new ContactInfo("Test", "Test@test.com", "Test address");
    }

    @Test
    void createRouteWithTwoLocations() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.MEL);
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 8, 0);

        DeliveryRoute route = service.createRoute(locations, departure);

        assertNotNull(route);
        assertEquals(2, route.getStops().size());
        assertEquals(877, route.getTotalDistanceKm()); // SYD to MEL distance
    }

    @Test
    void createRouteWithMultipleLocations() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.MEL, Location.ADL);
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 8, 0);

        DeliveryRoute route = service.createRoute(locations, departure);

        assertNotNull(route);
        assertEquals(3, route.getStops().size());
        assertEquals(1602, route.getTotalDistanceKm()); // SYD->MEL (877) + MEL->ADL (725)
    }

    @Test
    void createRouteCalculatesArrivalTimes() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.MEL);
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 8, 0);

        DeliveryRoute route = service.createRoute(locations, departure);

        RouteStop firstStop = route.getStops().get(0);
        RouteStop secondStop = route.getStops().get(1);

        assertAll(
                () -> assertTrue(firstStop.isDeparture()),
                () -> assertEquals(departure, firstStop.getTime()),
                () -> assertFalse(secondStop.isDeparture()),
                () -> assertTrue(secondStop.getTime().isAfter(departure))
        );
    }

    @Test
    void createRouteThrowsOnNullLocations() {
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 8, 0);

        assertThrows(InvalidInputException.class,
                () -> service.createRoute(null, departure));
    }

    @Test
    void createRouteThrowsOnSingleLocation() {
        List<Location> locations = Arrays.asList(Location.SYD);
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 8, 0);

        assertThrows(InvalidInputException.class,
                () -> service.createRoute(locations, departure));
    }

    @Test
    void createRouteThrowsOnEmptyLocations() {
        List<Location> locations = Arrays.asList();
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 8, 0);

        assertThrows(InvalidInputException.class,
                () -> service.createRoute(locations, departure));
    }

    @Test
    void createRouteThrowsOnNullDepartureTime() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.MEL);

        assertThrows(InvalidInputException.class,
                () -> service.createRoute(locations, null));
    }

    @Test
    void findByIdReturnsExistingRoute() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.MEL);
        LocalDateTime departure = LocalDateTime.of(2024, 10, 10, 8, 0);
        DeliveryRoute created = service.createRoute(locations, departure);

        Optional<DeliveryRoute> found = service.findById(created.getId());
        assertTrue(found.isPresent());
        assertEquals(created.getId(), found.get().getId());
    }

    @Test
    void findByIdReturnsEmptyForNonExistent() {
        Optional<DeliveryRoute> found = service.findById(9999);
        assertFalse(found.isPresent());
    }

    @Test
    void findAllReturnsAllRoutes() {
        service.createRoute(Arrays.asList(Location.SYD, Location.MEL), LocalDateTime.now());
        service.createRoute(Arrays.asList(Location.MEL, Location.ADL), LocalDateTime.now());
        service.createRoute(Arrays.asList(Location.BRI, Location.SYD), LocalDateTime.now());

        List<DeliveryRoute> routes = service.findAll();
        assertEquals(3, routes.size());
    }

    @Test
    void findAllReturnsEmptyWhenNoRoutes() {
        List<DeliveryRoute> routes = service.findAll();
        assertNotNull(routes);
        assertEquals(0, routes.size());
    }

    @Test
    void assignTruckSuccessfully() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.MEL);
        DeliveryRoute route = service.createRoute(locations, LocalDateTime.now());
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);

        assertDoesNotThrow(() -> service.assignTruck(route, truck));

        assertAll(
                () -> assertTrue(truck.isAssigned()),
                () -> assertEquals(truck, route.getAssignedTruck())
        );
    }

    @Test
    void assignTruckThrowsWhenTruckAlreadyAssigned() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.MEL);
        DeliveryRoute route = service.createRoute(locations, LocalDateTime.now());
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        truck.setAssigned(true);

        assertThrows(InvalidInputException.class,
                () -> service.assignTruck(route, truck));
    }

    @Test
    void assignTruckThrowsWhenRouteAlreadyHasTruck() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.MEL);
        DeliveryRoute route = service.createRoute(locations, LocalDateTime.now());
        Truck truck1 = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        Truck truck2 = new Truck(1002, TruckModel.SCANIA, 42000, 8000);

        service.assignTruck(route, truck1);

        assertThrows(InvalidInputException.class,
                () -> service.assignTruck(route, truck2));
    }

    @Test
    void assignTruckThrowsWhenRangeInsufficient() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.PER); // 4016 km
        DeliveryRoute route = service.createRoute(locations, LocalDateTime.now());
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 3000); // Only 3000 km range

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> service.assignTruck(route, truck));

        assertTrue(exception.getMessage().contains("insufficient"));
    }

    @Test
    void assignPackageSuccessfully() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.MEL, Location.ADL);
        DeliveryRoute route = service.createRoute(locations, LocalDateTime.now());
        DeliveryPackage pkg = new DeliveryPackage(1, Location.SYD, Location.MEL, 100, testContact());

        assertDoesNotThrow(() -> service.assignPackage(route, pkg));

        assertAll(
                () -> assertEquals(PackageStatus.ASSIGNED, pkg.getStatus()),
                () -> assertNotNull(pkg.getExpectedArrival()),
                () -> assertTrue(route.getPackages().contains(pkg))
        );
    }


    @Test
    void assignMultiplePackagesToRoute() {
        List<Location> locations = Arrays.asList(Location.SYD, Location.MEL, Location.ADL);
        DeliveryRoute route = service.createRoute(locations, LocalDateTime.now());
        Truck truck = new Truck(1001, TruckModel.SCANIA, 42000, 8000);
        service.assignTruck(route, truck);

        DeliveryPackage pkg1 = new DeliveryPackage(1, Location.SYD, Location.MEL, 100, testContact());
        DeliveryPackage pkg2 = new DeliveryPackage(2, Location.MEL, Location.ADL, 200, testContact());

        service.assignPackage(route, pkg1);
        service.assignPackage(route, pkg2);

        assertEquals(2, route.getPackages().size());
        assertEquals(300.0, route.getTotalPackageWeight(), 0.01);
    }
}
