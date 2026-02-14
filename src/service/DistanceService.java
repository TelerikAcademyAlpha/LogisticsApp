package service;

import enums.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistanceService {

    private static final Map<String, Integer> DISTANCES = new HashMap<>();

    //this static block will run only once when the program starts
    static {
        addDistance(Location.SYD, Location.MEL, 877);
        addDistance(Location.SYD, Location.ADL, 1376);
        addDistance(Location.SYD, Location.ASP, 2762);
        addDistance(Location.SYD, Location.BRI, 909);
        addDistance(Location.SYD, Location.DAR, 3935);
        addDistance(Location.SYD, Location.PER, 4016);
        addDistance(Location.MEL, Location.ADL, 725);
        addDistance(Location.MEL, Location.ASP, 2255);
        addDistance(Location.MEL, Location.BRI, 1765);
        addDistance(Location.MEL, Location.DAR, 3752);
        addDistance(Location.MEL, Location.PER, 3509);
        addDistance(Location.ADL, Location.ASP, 1530);
        addDistance(Location.ADL, Location.BRI, 1927);
        addDistance(Location.ADL, Location.DAR, 3027);
        addDistance(Location.ADL, Location.PER, 2785);
        addDistance(Location.ASP, Location.BRI, 2993);
        addDistance(Location.ASP, Location.DAR, 1497);
        addDistance(Location.ASP, Location.PER, 2481);
        addDistance(Location.BRI, Location.DAR, 3426);
        addDistance(Location.BRI, Location.PER, 4311);
        addDistance(Location.DAR, Location.PER, 4025);
    }

    private static void addDistance(Location from, Location to, int km) {
        DISTANCES.put(key(from, to), km);
        DISTANCES.put(key(to, from), km);
    }

    private static String key(Location from, Location to) {
        return from.name() + "-" + to.name();
    }

    /**
     * Returns the distance in km between two locations.
     *
     * @throws IllegalArgumentException if the distance is not defined
     */
    public int getDistance(Location from, Location to) {
        if (from == to) return 0;
        Integer distance = DISTANCES.get(key(from, to));
        if (distance == null) {
            throw new IllegalArgumentException(
                    "No known distance between " + from + " and " + to);
        }
        return distance;
    }

    /**
     * Calculates total route distance for a list of locations in order.
     */
    public int calculateTotalDistance(List<Location> locations) {
        int total = 0;
        for (int i = 0; i < locations.size() - 1; i++) {
            total += getDistance(locations.get(i), locations.get(i + 1));
        }
        return total;
    }
}
