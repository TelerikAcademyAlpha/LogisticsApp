package repository;

import model.InMemoryData;

/**
* Centralized interface providing access to the shared in-memory database
* */
public interface InMemoryDataStore {
    InMemoryData getData();

    void setData(InMemoryData date);
}
