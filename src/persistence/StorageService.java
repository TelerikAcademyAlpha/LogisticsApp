package persistence;

import model.InMemoryData;

public interface StorageService {
    void save(InMemoryData data) throws Exception;

    InMemoryData load() throws Exception;

    boolean hasSavedData();
}
