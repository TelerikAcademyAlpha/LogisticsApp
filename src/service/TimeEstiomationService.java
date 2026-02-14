package service;

import enums.Location;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeEstiomationService {

    public static final double AVERAGE_SPEED_KMH = 87.0;

    private final DistanceService distanceService;

    public TimeEstiomationService(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    /**
     * Calculates estimated arrival times for each stop after the departure.
     *
     * @param locations     ordered list of locations
     * @param departureTime departure time from the first location
     * @return list of LocalDateTimes (first is departure, rest are arrivals)
     */
    public List<LocalDateTime> calculateArrivalTimes(List<Location> locations, LocalDateTime departureTime) {
        List<LocalDateTime> times = new ArrayList<>();
        times.add(departureTime);

        LocalDateTime currentTime = departureTime;
        for (int i = 0; i < locations.size() - 1; i++) {
            int distance = distanceService.getDistance(locations.get(i), locations.get(i + 1));
            double hours = distance / AVERAGE_SPEED_KMH;
            long minutes = Math.round(hours * 60);
            currentTime = currentTime.plusMinutes(minutes);
            times.add(currentTime);
        }
        return times;
    }
}
