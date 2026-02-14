package service;

import enums.Location;
import enums.PackageStatus;
import exception.InvalidInputException;
import model.ContactInfo;
import model.DeliveryPackage;
import repository.PackageRepository;

import java.util.List;
import java.util.Optional;

public class PackageService {

    private final PackageRepository packageRepository;

    public PackageService(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    /**
     * Creates a new delivery package with auto-generated ID.
     */
    public DeliveryPackage createPackage(Location startLocation, Location endLocation,
                                         double weight, ContactInfo contactInfo) {
        if (startLocation == null || endLocation == null) {
            throw new InvalidInputException("Start and end locations are required.");
        }
        if (weight <= 0) {
            throw new InvalidInputException("Weight must be a positive number.");
        }
        if (contactInfo == null) {
            throw new InvalidInputException("Contact info is required.");
        }

        int id = packageRepository.generateId();
        DeliveryPackage pkg = new DeliveryPackage(id, startLocation, endLocation, weight, contactInfo, null,PackageStatus.UNASSIGNED);
        packageRepository.save(pkg);
        return pkg;
    }

    public Optional<DeliveryPackage> findById(int id) {
        return packageRepository.findById(id);
    }

    public List<DeliveryPackage> findAll() {
        return packageRepository.findAll();
    }

    public List<DeliveryPackage> findUnassigned() {
        return packageRepository.findByStatus(PackageStatus.UNASSIGNED);
    }

    /**
     * Marks a package as assigned to a route.
     */
    public void markAssigned(DeliveryPackage pkg, java.time.LocalDateTime expectedArrival) {
        pkg.setStatus(PackageStatus.ASSIGNED);
        pkg.setExpectedArrival(expectedArrival);
    }
}
