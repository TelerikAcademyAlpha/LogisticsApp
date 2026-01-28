package model;

import enums.Location;

import java.time.LocalDateTime;

public class RouteStop {
    private Location location;
    private LocalDateTime time;

    public RouteStop(Location location ,LocalDateTime time) {
        this.location = location;
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "RouteStop{" +
                "location=" + location +
                ", time=" + time +
                '}';
    }
}
