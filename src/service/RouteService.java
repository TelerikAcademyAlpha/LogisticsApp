package service;

import enums.Location;
import enums.PackageStatus;
import exception.InvalidInputException;
import model.*;
import repository.RouteRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RouteService {

    private final RouteRepository routeRepository;
    private final DistanceService distanceService;
    private final TimeEstiomationService timeService;

    public RouteService(RouteRepository routeRepository, DistanceService distanceService,
                        TimeEstiomationService timeService) {
        this.routeRepository = routeRepository;
        this.distanceService = distanceService;
        this.timeService = timeService;
    }

    /**
     * Creates a new delivery route with calculated distances and arrival times.
     */
    public DeliveryRoute createRoute(List<Location> locations, LocalDateTime departureTime) {
        if (locations == null || locations.size() < 2) {
            throw new InvalidInputException("A route must have at least two locations.");
        }
        if (departureTime == null) {
            throw new InvalidInputException("Departure time is required.");
        }

        int totalDistance = distanceService.calculateTotalDistance(locations);
        List<LocalDateTime> times = timeService.calculateArrivalTimes(locations, departureTime);

        List<RouteStop> stops = new ArrayList<>();
        stops.add(new RouteStop(locations.get(0), times.get(0), true)); // departure

        for (int i = 1; i < locations.size(); i++) {
            stops.add(new RouteStop(locations.get(i), times.get(i), false)); // arrivals
        }

        int id = routeRepository.generateId();
        DeliveryRoute route = new DeliveryRoute(id, stops, totalDistance);
        routeRepository.save(route);
        return route;
    }

    public Optional<DeliveryRoute> findById(int id) {
        return routeRepository.findById(id);
    }

    public List<DeliveryRoute> findAll() {
        return routeRepository.findAll();
    }

    /**
     * Searches for routes that serve both the start and end locations in order.
     */
    public List<DeliveryRoute> searchRoutes(Location start, Location end) {
        return routeRepository.findByLocations(start, end);
    }

    /**
     * Returns all routes currently in progress.
     */
    public List<DeliveryRoute> findInProgress() {
        LocalDateTime now = LocalDateTime.now();
        return routeRepository.findAll().stream()
                .filter(r -> r.isInProgress(now))
                .collect(Collectors.toList());
    }

    /**
     * Assigns a truck to a route with validation.
     */
    public void assignTruck(DeliveryRoute route, Truck truck) {
        if (truck.isAssigned()) {
            throw new InvalidInputException("Truck #" + truck.getId() + " is already assigned.");
        }
        if (route.getAssignedTruck() != null) {
            throw new InvalidInputException("Route #" + route.getId() + " already has a truck assigned.");
        }
        if (truck.getMaxRangeKm() < route.getTotalDistanceKm()) {
            throw new InvalidInputException(
                    String.format("Truck #%d range (%,d km) is insufficient for route distance (%,d km).",
                            truck.getId(), truck.getMaxRangeKm(), route.getTotalDistanceKm()));
        }

        truck.setAssigned(true);
        route.setAssignedTruck(truck);
    }

    /**
     * Assigns a package to a route with validation.
     */
    public void assignPackage(DeliveryRoute route, DeliveryPackage pkg) {
        if (pkg.getStatus() != PackageStatus.UNASSIGNED) {
            throw new InvalidInputException("Package #" + pkg.getId() + " is already assigned (status: " + pkg.getStatus() + ").");
        }
        if (!route.servesLocations(pkg.getStartLocation(), pkg.getEndLocation())) {
            throw new InvalidInputException(
                    String.format("Route #%d does not serve %s → %s.",
                            route.getId(), pkg.getStartLocation(), pkg.getEndLocation()));
        }

        // Check capacity if truck is assigned
        if (route.getAssignedTruck() != null) {
            double currentWeight = route.getTotalPackageWeight();
            if (currentWeight + pkg.getWeight() > route.getAssignedTruck().getCapacityKg()) {
                throw new InvalidInputException(
                        String.format("Adding package (%.1f kg) would exceed truck capacity. Current: %.1f / %,d kg.",
                                pkg.getWeight(), currentWeight, route.getAssignedTruck().getCapacityKg()));
            }
        }

        LocalDateTime arrivalTime = route.getArrivalTimeAt(pkg.getEndLocation());
        pkg.setStatus(PackageStatus.ASSIGNED);
        pkg.setExpectedArrival(arrivalTime);
        route.addPackage(pkg);
    }
}
