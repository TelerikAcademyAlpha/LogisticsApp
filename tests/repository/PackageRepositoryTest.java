package repository;

import enums.Location;
import enums.PackageStatus;
import model.ContactInfo;
import model.DeliveryPackage;
import model.InMemoryData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PackageRepositoryTest {
    private InMemoryDataStoreImpl dataStore;
    private PackageRepository repository;

    @BeforeEach
    void setUp() {
        dataStore = InMemoryDataStoreImpl.getInstance();
        dataStore.setData(new InMemoryData());
        repository = new PackageRepositoryImpl(dataStore);
    }

    private DeliveryPackage pkg(int id) {
        return new DeliveryPackage(id, Location.SYD, Location.MEL, 10, new ContactInfo("A", "B", "C"));
    }

    @Test
    void saveAndFindById() {
        repository.save(pkg(1));
        assertTrue(repository.findById(1).isPresent());
        assertEquals(1, repository.findById(1).get().getId());
    }

    @Test
    void findByIdNotFound() {
        assertFalse(repository.findById(999).isPresent());
    }

    @Test
    void findAll() {
        repository.save(pkg(1));
        repository.save(pkg(2));
        repository.save(pkg(3));
        assertEquals(3, repository.findAll().size());
    }

    @Test
    void findByStatus() {
        DeliveryPackage p1 = pkg(1);
        DeliveryPackage p2 = pkg(2);
        DeliveryPackage p3 = pkg(3);
        p2.setStatus(PackageStatus.ASSIGNED);
        repository.save(p1);
        repository.save(p2);
        repository.save(p3);
        assertEquals(2, repository.findByStatus(PackageStatus.UNASSIGNED).size());
        assertEquals(1, repository.findByStatus(PackageStatus.ASSIGNED).size());
    }

    @Test
    void generateIdIncrements() {
        assertEquals(1, repository.generateId());
        assertEquals(2, repository.generateId());
        assertEquals(3, repository.generateId());
    }

    @Test
    void saveUpdatesExistingPackage() {
        DeliveryPackage p = pkg(1);
        repository.save(p);
        p.setWeight(999);
        repository.save(p);
        assertEquals(1, repository.findAll().size());
        assertEquals(999.0, repository.findById(1).get().getWeight(), 0.01);
    }
}
