package model;

import enums.Location;
import util.DateTimeFormatterUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RouteStop implements Serializable {
    private static final long serialVersionUID = 1L;

    private Location location;
    private LocalDateTime time;
    private boolean isDeparture;

    public RouteStop(Location location, LocalDateTime time, boolean isDeparture) {
        this.location = location;
        this.time = time;
        this.isDeparture = isDeparture;
    }

    public RouteStop(Location location, LocalDateTime time) {
        this(location, time, false);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public boolean isDeparture() {
        return isDeparture;
    }

    public void setDeparture(boolean departure) {
        isDeparture = departure;
    }

    @Override
    public String toString() {
        String label = isDeparture ? "Depart" : "Arrive";
        return String.format("%s - %s: %s", location, label, DateTimeFormatterUtil.format(time));
    }
}
