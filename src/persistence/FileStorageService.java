package persistence;

import model.InMemoryData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStorageService implements StorageService {

    private static final String STORAGE_DIR = "storage";
    private static final String STORAGE_FILE = "logistics_storage.dat";

    @Override
    public void save(InMemoryData data) throws IOException {
        Path dir = Paths.get(STORAGE_DIR);
        if (!Files.exists(dir)){
            Files.createDirectories(dir);
        }
        Path filePath = dir.resolve(STORAGE_FILE);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))){
            out.writeObject(data);
        }
        System.out.println("Data save with success to " + filePath.toAbsolutePath());
    }

    @Override
    public InMemoryData load() throws Exception {
        Path filepath = Paths.get(STORAGE_DIR, STORAGE_FILE);
        if (!Files.exists(filepath)){

            return null;
        }
        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream(filepath.toFile()))){
            InMemoryData data = (InMemoryData) out.readObject();
            System.out.println("Data loaded successfully " + filepath.toAbsolutePath());
            return data;
        }
    }

    @Override
    public boolean hasSavedData() {
        return Files.exists(Paths.get(STORAGE_DIR, STORAGE_FILE));
    }
}
