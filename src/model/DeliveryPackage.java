package model;

import enums.Location;
import enums.PackageStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DeliveryPackage implements Serializable{
    private static final long serialVersionUID = 1L;

    private final int id;
    private Location startLocation;
    private Location endLocation;
    private double weight;
    private ContactInfo contactInfo;
    private LocalDateTime expectedArrival;
    private PackageStatus status;

    public DeliveryPackage(int id, Location startLocation, Location endLocation, double weight, ContactInfo contactInfo, LocalDateTime expectedArrival, PackageStatus status) {
        this.id = id;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.weight = weight;
        this.contactInfo = contactInfo;
        this.expectedArrival = expectedArrival;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public LocalDateTime getExpectedArrival() {
        return expectedArrival;
    }

    public void setExpectedArrival(LocalDateTime expectedArrival) {
        this.expectedArrival = expectedArrival;
    }

    public PackageStatus getStatus() {
        return status;
    }

    public void setStatus(PackageStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Package #%d%n", id));
        sb.append(String.format(" From:     %s%n", startLocation));
        sb.append(String.format(" To:       %s%n", endLocation));
        sb.append(String.format(" Weight:   %.1f kg%n", weight));
        sb.append(String.format(" Contact:  %s%n", contactInfo));
        sb.append(String.format(" Status:   %s%n", status));
        if (expectedArrival != null) {
            sb.append(String.format(" Expected: %s%n", util.DateTimeFormatterUtil.format(expectedArrival)));
        }
        return sb.toString();
    }
}
