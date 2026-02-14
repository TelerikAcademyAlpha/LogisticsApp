package model;

import enums.Location;
import util.DateTimeFormatterUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryRoute implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private List<RouteStop> stops;
    private int totalDistanceKm;
    private Truck assignedTruck;
    private List<DeliveryPackage> packages;

    public DeliveryRoute(int id, Truck assignedTruck) {
        this.id = id;
        this.stops = new ArrayList<>();
        this.assignedTruck = assignedTruck;
        this.totalDistanceKm = 0;
        this.packages = new ArrayList<>();
    }

    public DeliveryRoute(int id, List<RouteStop> stops, int totalDistanceKm) {
        this.id = id;
        this.stops = new ArrayList<>(stops);
        this.totalDistanceKm = totalDistanceKm;
        this.assignedTruck = null;
        this.packages = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<RouteStop> getStops() {
        return Collections.unmodifiableList(stops);
    }

    public void setStops(List<RouteStop> stops) {
        this.stops = new ArrayList<>(stops);
    }

    public int getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(int totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public Truck getAssignedTruck() {
        return assignedTruck;
    }

    public void setAssignedTruck(Truck assignedTruck) {
        this.assignedTruck = assignedTruck;
    }

    public List<DeliveryPackage> getPackages() {
        return Collections.unmodifiableList(packages);
    }

    public void setPackages(List<DeliveryPackage> packages) {
        this.packages = new ArrayList<>(packages);
    }

    public void addPackage(DeliveryPackage dp) {
        if (packages.stream().noneMatch(p -> p.getId() == dp.getId())) {
            packages.add(dp);
        }
    }

    public void removePackage(int packageId) {
        packages.removeIf(p -> p.getId() == packageId);
    }

    public Location getStartLocation() {
        return stops.isEmpty() ? null : stops.get(0).getLocation();
    }

    public Location getEndLocation() {
        return stops.isEmpty() ? null : stops.get(stops.size() - 1).getLocation();
    }

    public LocalDateTime getDepartureTime() {
        return stops.isEmpty() ? null : stops.get(0).getTime();
    }

    public LocalDateTime getArrivalTime(){
        return stops.isEmpty() ? null : stops.get(stops.size() - 1).getTime();
    }

    public double getFreeCapacity() {
        if (assignedTruck == null) {
            return Double.MAX_VALUE;
        }
        return assignedTruck.getCapacityKg() - getTotalPackageWeight();
    }

    public double getTotalPackageWeight() {
        return packages.stream()
                .mapToDouble(DeliveryPackage::getWeight)
                .sum();
    }

    public LocalDateTime getArrivalTimeAt(Location location) {
        return stops.stream()
                .filter(s -> s.getLocation() == location && !s.isDeparture())
                .map(RouteStop::getTime)
                .findFirst()
                .orElse(null);
    }

    public String getCurrentStopInfo(LocalDateTime now) {
        if (stops.isEmpty()) {
            return "No stops defined";
        }
        if (now.isBefore(getDepartureTime())) {
            return "Not yet departed from " + getStartLocation();
        }
        if (now.isAfter(getArrivalTime())) {
            return "Route completed - arrived at " + getEndLocation();
        }

        for (int i = stops.size() - 1; i >= 0; i--) {
            if (!now.isBefore(stops.get(i).getTime())) {
                if (i < stops.size() - 1) {
                    return String.format("En route from %s to %s",
                            stops.get(i).getLocation(), stops.get(i + 1).getLocation());
                } else {
                    return "Arrived at " + stops.get(i).getLocation();
                }
            }
        }
        return "Unknown";
    }

    public boolean isInProgress(LocalDateTime now) {
        if (stops.isEmpty()) return false;
        return !now.isBefore(getDepartureTime()) && !now.isAfter(getArrivalTime());
    }

    public boolean servesLocations(Location start, Location end) {
        int startIndex = -1;
        int endIndex = -1;
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).getLocation() == start && startIndex == -1) {
                startIndex = i;
            }
            if (stops.get(i).getLocation() == end && startIndex != -1) {
                endIndex = i;
                break;
            }
        }
        return startIndex != -1 && endIndex != -1 && endIndex > startIndex;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Route #%d%n", id));
        if (!stops.isEmpty()) {
            sb.append(String.format("  Stops: %s%n",
                    stops.stream()
                            .map(s -> s.getLocation().getFullName())
                            .collect(Collectors.joining(" → "))));
            sb.append(String.format("  Distance:  %,d km%n", totalDistanceKm));
            sb.append(String.format("  Departure: %s%n", DateTimeFormatterUtil.format(getDepartureTime())));
            sb.append(String.format("  Arrival:   %s%n", DateTimeFormatterUtil.format(getArrivalTime())));
        }
        sb.append(String.format("  Truck:     %s%n", assignedTruck != null ? "#" + assignedTruck.getId() + " [" + assignedTruck.getModel() + "]" : "None"));
        sb.append(String.format("  Packages:  %d (%.1f kg)%n", packages.size(), getTotalPackageWeight()));
        return sb.toString();
    }

    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("*********** Route #%d ***********%n", id));
        if (!stops.isEmpty()) {
            sb.append("  Stops:\n");
            for (RouteStop stop : stops) {
                sb.append("    ").append(stop).append("\n");
            }
            sb.append(String.format("  Total Distance: %,d km%n", totalDistanceKm));
        }
        sb.append(String.format("  Truck: %s%n", assignedTruck != null ? assignedTruck : "Not assigned"));
        sb.append(String.format("  Packages (%d): %.1f kg total%n", packages.size(), getTotalPackageWeight()));
        if (!packages.isEmpty()) {
            packages.forEach(p -> sb.append(String.format("    - Package #%d: %s → %s (%.1f kg)%n",
                    p.getId(), p.getStartLocation().getFullName(),
                    p.getEndLocation().getFullName(), p.getWeight())));
        }
        return sb.toString();
    }
}
