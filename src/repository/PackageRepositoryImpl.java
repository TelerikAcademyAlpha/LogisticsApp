package repository;

import enums.PackageStatus;
import model.DeliveryPackage;
import model.InMemoryData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PackageRepositoryImpl implements PackageRepository{
    private final InMemoryDataStore dataStore;

    public PackageRepositoryImpl(InMemoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void save(DeliveryPackage pkg) {
        InMemoryData data = dataStore.getData();
        data.getPackages().removeIf(p -> p.getId() == pkg.getId());
        data.getPackages().add(pkg);
    }

    @Override
    public Optional<DeliveryPackage> findById(int id) {
        return dataStore.getData().getPackages().stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public List<DeliveryPackage> findAll() {
        return dataStore.getData().getPackages();
    }

    @Override
    public List<DeliveryPackage> findByStatus(PackageStatus status) {
        return dataStore.getData().getPackages().stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public int generateId() {
        return dataStore.getData().generatePackageId();
    }
}
