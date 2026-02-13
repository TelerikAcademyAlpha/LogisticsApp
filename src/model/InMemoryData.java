package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InMemoryData implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<DeliveryPackage> packages;
    private List<DeliveryRoute> routes;
    private List<Truck> trucks;

    private int nextPackageId;
    private int nextRouteId;

    public InMemoryData() {
        this.packages = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.trucks = new ArrayList<>();
        this.nextPackageId = 1;
        this.nextRouteId = 1;
    }

    public List<DeliveryPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<DeliveryPackage> packages) {
        this.packages = packages;
    }

    public List<DeliveryRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<DeliveryRoute> routes) {
        this.routes = routes;
    }

    public List<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }

    public int getNextPackageId() {
        return nextPackageId;
    }

    public void setNextPackageId(int nextPackageId) {
        this.nextPackageId = nextPackageId;
    }

    public int getNextRouteId() {
        return nextRouteId;
    }

    public void setNextRouteId(int nextRouteId) {
        this.nextRouteId = nextRouteId;
    }

    public int generatePackageId() {
        return nextPackageId++;
    }

    public int generateRouteId() {
        return nextRouteId++;
    }
}
