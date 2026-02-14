package model;

import enums.Location;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeliveryRoute implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
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

    //todo
}
