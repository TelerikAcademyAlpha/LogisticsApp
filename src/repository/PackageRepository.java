package repository;

import enums.PackageStatus;
import model.DeliveryPackage;

import java.util.List;
import java.util.Optional;

public interface PackageRepository {
    public void save(DeliveryPackage pkg);

    public Optional<DeliveryPackage> findById(int id);

    public List<DeliveryPackage> findAll();

    public List<DeliveryPackage> findByStatus(PackageStatus status);

    public int generateId();

}
