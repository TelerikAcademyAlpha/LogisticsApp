package repository;

import enums.Location;
import enums.PackageStatus;
import model.ContactInfo;
import model.DeliveryPackage;
import model.InMemoryData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.PackageService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PackageRepositoryTest {
    private InMemoryDataStoreImpl dataStore;
    private PackageRepository repository;
    private PackageService service;

    @BeforeEach
    void setUp() {
        dataStore = InMemoryDataStoreImpl.getInstance();
        dataStore.setData(new InMemoryData());
        repository = new PackageRepositoryImpl(dataStore);
        service = new PackageService(repository);
    }

    private ContactInfo getTestContact() {
        return new ContactInfo("A", "B", "C");
    }

    @Test
    void saveAndFindById() {
        DeliveryPackage pkg = service.createPackage(Location.SYD, Location.MEL, 10.0, getTestContact());
        assertTrue(service.findById(pkg.getId()).isPresent());
        assertEquals(pkg.getId(), service.findById(pkg.getId()).get().getId());
    }

    @Test
    void findByIdNotFound() {
        assertFalse(service.findById(999).isPresent());
    }

    @Test
    void findAll() {
        service.createPackage(Location.SYD, Location.MEL, 10.0, getTestContact());
        service.createPackage(Location.MEL, Location.ADL, 15.0, getTestContact());
        service.createPackage(Location.BRI, Location.SYD, 20.0, getTestContact());
        assertEquals(3, service.findAll().size());
    }

    @Test
    void findByStatus() {
        DeliveryPackage p1 = service.createPackage(Location.SYD, Location.MEL, 10.0, getTestContact());
        DeliveryPackage p2 = service.createPackage(Location.MEL, Location.ADL, 15.0, getTestContact());
        DeliveryPackage p3 = service.createPackage(Location.BRI, Location.SYD, 20.0, getTestContact());

        // mark one as assigned
        service.markAssigned(p2, java.time.LocalDateTime.now());

        List<DeliveryPackage> unassigned = service.findUnassigned();
        assertEquals(2, unassigned.size());

        // verify through repository that we have the right statuses
        assertEquals(2, repository.findByStatus(PackageStatus.UNASSIGNED).size());
        assertEquals(1, repository.findByStatus(PackageStatus.ASSIGNED).size());
    }

    @Test
    void generateIdIncrements() {
        DeliveryPackage p1 = service.createPackage(Location.SYD, Location.MEL, 10.0, getTestContact());
        DeliveryPackage p2 = service.createPackage(Location.MEL, Location.ADL, 15.0, getTestContact());
        DeliveryPackage p3 = service.createPackage(Location.BRI, Location.SYD, 20.0, getTestContact());

        assertEquals(1, p1.getId());
        assertEquals(2, p2.getId());
        assertEquals(3, p3.getId());
    }

    @Test
    void saveUpdatesExistingPackage() {
        DeliveryPackage p = service.createPackage(Location.SYD, Location.MEL, 10.0, getTestContact());
        int id = p.getId();

        // saving using repository
        p.setWeight(999);
        repository.save(p);

        assertEquals(1, service.findAll().size());
        assertEquals(999.0, service.findById(id).get().getWeight(), 0.01);
    }
}
