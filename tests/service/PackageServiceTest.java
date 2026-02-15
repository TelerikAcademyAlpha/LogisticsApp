package service;

import enums.Location;
import enums.PackageStatus;
import exception.InvalidInputException;
import model.ContactInfo;
import model.DeliveryPackage;
import model.InMemoryData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryDataStoreImpl;
import repository.PackageRepositoryImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PackageServiceTest {

    private PackageService service;

    @BeforeEach
    void setUp() {
        InMemoryDataStoreImpl store = InMemoryDataStoreImpl.getInstance();
        store.setData(new InMemoryData());
        service = new PackageService(new PackageRepositoryImpl(store));
    }

    private ContactInfo contact() { return new ContactInfo("A", "B", "C"); }

    @Test
    void createPackage() {
        DeliveryPackage pkg = service.createPackage(Location.SYD, Location.MEL, 45, contact());
        assertAll(
                () -> assertEquals(1, pkg.getId()),
                () -> assertEquals(Location.SYD, pkg.getStartLocation()),
                () -> assertEquals(Location.MEL, pkg.getEndLocation()),
                () -> assertEquals(45.0, pkg.getWeight(), 0.01),
                () -> assertEquals(PackageStatus.UNASSIGNED, pkg.getStatus())
        );
    }

    @Test
    void autoIncrementId() {
        assertEquals(1, service.createPackage(Location.SYD, Location.MEL, 10, contact()).getId());
        assertEquals(2, service.createPackage(Location.MEL, Location.ADL, 20, contact()).getId());
        assertEquals(3, service.createPackage(Location.BRI, Location.PER, 30, contact()).getId());
    }

    @Test
    void findById() {
        service.createPackage(Location.SYD, Location.MEL, 10, contact());
        assertTrue(service.findById(1).isPresent());
        assertFalse(service.findById(999).isPresent());
    }

    @Test
    void findAll() {
        service.createPackage(Location.SYD, Location.MEL, 10, contact());
        service.createPackage(Location.MEL, Location.ADL, 20, contact());
        assertEquals(2, service.findAll().size());
    }

    @Test
    void findUnassigned() {
        service.createPackage(Location.SYD, Location.MEL, 10, contact());
        service.createPackage(Location.MEL, Location.ADL, 20, contact());
        assertEquals(2, service.findUnassigned().size());
    }

    @Test
    void findUnassignedAfterAssign() {
        service.createPackage(Location.SYD, Location.MEL, 10, contact());
        service.createPackage(Location.MEL, Location.ADL, 20, contact());
        service.findById(1).get().setStatus(PackageStatus.ASSIGNED);
        assertEquals(1, service.findUnassigned().size());
    }

    @Test
    void markAssigned() {
        DeliveryPackage pkg = service.createPackage(Location.SYD, Location.MEL, 10, contact());
        LocalDateTime arrival = LocalDateTime.of(2024, 10, 11, 18, 0);
        service.markAssigned(pkg, arrival);
        assertAll(
                () -> assertEquals(PackageStatus.ASSIGNED, pkg.getStatus()),
                () -> assertEquals(arrival, pkg.getExpectedArrival())
        );
    }

    @Test
    void createNullStartThrows() {
        assertThrows(InvalidInputException.class,
                () -> service.createPackage(null, Location.MEL, 10, contact()));
    }

    @Test
    void createZeroWeightThrows() {
        assertThrows(InvalidInputException.class,
                () -> service.createPackage(Location.SYD, Location.MEL, 0, contact()));
    }

    @Test
    void createNegativeWeightThrows() {
        assertThrows(InvalidInputException.class,
                () -> service.createPackage(Location.SYD, Location.MEL, -5, contact()));
    }

    @Test
    void createNullContactThrows() {
        assertThrows(InvalidInputException.class,
                () -> service.createPackage(Location.SYD, Location.MEL, 10, null));
    }
}
