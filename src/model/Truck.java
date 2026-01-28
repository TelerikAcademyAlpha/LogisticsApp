package model;

import enums.TruckModel;

public class Truck {
    private final int id;
    private TruckModel model;
    private int capacityKg;
    private int maxRangeKm;
    private boolean assigned;

    public Truck(int id, TruckModel model, int capacityKg, int maxRangeKm) {
        this.id = id;
        this.model = model;
        this.capacityKg = capacityKg;
        this.maxRangeKm = maxRangeKm;
        this.assigned = false;
    }

    public int getId() {
        return id;
    }

    public TruckModel getModel() {
        return model;
    }

    public void setModel(TruckModel model) {
        this.model = model;
    }

    public int getCapacityKg() {
        return capacityKg;
    }

    public void setCapacityKg(int capacityKg) {
        this.capacityKg = capacityKg;
    }

    public int getMaxRangeKm() {
        return maxRangeKm;
    }

    public void setMaxRangeKm(int maxRangeKm) {
        this.maxRangeKm = maxRangeKm;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "id=" + id +
                ", model=" + model +
                ", capacityKg=" + capacityKg +
                ", maxRangeKm=" + maxRangeKm +
                ", assigned=" + assigned +
                '}';
    }
}
