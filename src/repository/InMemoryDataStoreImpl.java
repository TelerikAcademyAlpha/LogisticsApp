package repository;

import model.InMemoryData;

public class InMemoryDataStoreImpl implements InMemoryDataStore {

    /**
     * Singleton-pattern of SOLID principles implementation used for in-memory database
     */
    private static InMemoryDataStoreImpl instance;
    private InMemoryData data;

    private InMemoryDataStoreImpl() {
        this.data = new InMemoryData();
    }

    public static InMemoryDataStoreImpl getInstance() {
        if (instance == null) {
            instance = new InMemoryDataStoreImpl();
        }
        return instance;
    }

    @Override
    public InMemoryData getData() {
        return data;
    }

    @Override
    public void setData(InMemoryData data) {
        this.data = data;
    }
}
