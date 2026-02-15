package persistence;

import enums.Location;
import model.ContactInfo;
import model.DeliveryPackage;
import model.InMemoryData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileStorageServiceTest {

    private FileStorageService storage;

    public static final String STORAGE_FILE = "logistics_storage.dat";


    @BeforeEach
    void setUp() {
        storage = new FileStorageService();
        cleanup();
    }

    @AfterEach
    void tearDown() {
        cleanup();
    }

    private void cleanup() {
        try {
            Files.deleteIfExists(Paths.get("storage", STORAGE_FILE));
        } catch (Exception ignored) {}
    }

    @Test
    void saveAndLoadNotNull() throws Exception {
        storage.save(new InMemoryData());
        assertNotNull(storage.load());
    }

    @Test
    void preservesPackages() throws Exception {
        InMemoryData data = new InMemoryData();
        data.getPackages().add(new DeliveryPackage(1, Location.SYD, Location.MEL, 45, new ContactInfo("A", "B", "1")));
        data.getPackages().add(new DeliveryPackage(2, Location.BRI, Location.PER, 100, new ContactInfo("C", "D", "2")));
        storage.save(data);
        assertEquals(2, storage.load().getPackages().size());
    }

    @Test
    void preservesIdCounters() throws Exception{
        InMemoryData data = new InMemoryData();
        data.generatePackageId();
        data.generatePackageId();
        storage.save(data);
        assertEquals(3, storage.load().getNextPackageId());
    }

    @Test
    void hasSavedData() throws Exception{
        assertFalse(storage.hasSavedData());
        storage.save(new InMemoryData());
        assertTrue(storage.hasSavedData());
    }

    @Test
    void loadReturnsNullWhenNoFile() throws Exception{
        assertNull(storage.load());
    }
}
