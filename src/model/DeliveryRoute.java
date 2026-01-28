package model;

import java.util.ArrayList;
import java.util.List;

public class DeliveryRoute {
    private int id;
    private Truck assignedTruck;
    private List<DeliveryPackage> packages;

    public DeliveryRoute(int id, Truck assignedTruck) {
        this.id = id;
        this.assignedTruck = assignedTruck;
        this.packages = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Truck getAssignedTruck() {
        return assignedTruck;
    }

    public void setAssignedTruck(Truck assignedTruck) {
        this.assignedTruck = assignedTruck;
    }

    public List<DeliveryPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<DeliveryPackage> packages) {
        this.packages = packages;
    }

    @Override
    public String toString() {
        return "DeliveryRoute{" +
                "id=" + id +
                ", assignedTruck=" + assignedTruck +
                ", packages=" + packages +
                '}';
    }
}
