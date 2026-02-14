package service;

import enums.Location;

import java.util.List;

public class DistanceService {

    private static final int[][] D = {
            {  0, 877,1376,2762, 909,3935,4016}, // SYD
            {877,  0, 725,2255,1765,3752,3509}, // MEL
            {1376,725,  0,1530,1927,3027,2785}, // ADL
            {2762,2255,1530,  0,2993,1497,2481}, // ASP
            { 909,1765,1927,2993,  0,3426,4311}, // BRI
            {3935,3752,3027,1497,3426,  0,4025}, // DAR
            {4016,3509,2785,2481,4311,4025,  0}  // PER
    };

    public int getDistance(Location from, Location to) {
        return D[from.ordinal()][to.ordinal()];
    }

    public int calculateTotalDistance(List<Location> locations) {
        int total = 0;
        for (int i = 0; i < locations.size() - 1; i++) {
            total += getDistance(locations.get(i), locations.get(i + 1));
        }
        return total;
    }
}