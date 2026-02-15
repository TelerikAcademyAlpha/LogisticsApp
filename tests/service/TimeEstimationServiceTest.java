package service;

import enums.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeEstimationServiceTest {
    private TimeEstiomationService service;

    @BeforeEach
    void setUp() {
        service = new TimeEstiomationService(new DistanceService());
    }

    @Test
    void twoStopReturnsCorrectCount() {
        LocalDateTime dep = LocalDateTime.of(2024, 10, 10, 6, 0);
        List<LocalDateTime> times = service.calculateArrivalTimes(
                Arrays.asList(Location.SYD, Location.MEL), dep);
        assertEquals(2, times.size());
    }

    @Test
    void departureTimePreserved() {
        LocalDateTime dep = LocalDateTime.of(2024, 1, 1, 0, 0);
        List<LocalDateTime> times = service.calculateArrivalTimes(
                Arrays.asList(Location.PER, Location.DAR), dep);
        assertEquals(dep, times.get(0));
    }

    @Test
    void arrivalIsAfterDeparture() {
        LocalDateTime dep = LocalDateTime.of(2024, 10, 10, 6, 0);
        List<LocalDateTime> times = service.calculateArrivalTimes(
                Arrays.asList(Location.SYD, Location.MEL), dep);
        assertTrue(times.get(1).isAfter(dep));
    }

    @Test
    void multiStopTimesIncreasing() {
        LocalDateTime dep = LocalDateTime.of(2024, 10, 10, 6, 0);
        List<LocalDateTime> times = service.calculateArrivalTimes(
                Arrays.asList(Location.SYD, Location.MEL, Location.ADL, Location.ASP), dep);
        assertEquals(4, times.size());
        for (int i = 1; i < times.size(); i++)
            assertTrue(times.get(i).isAfter(times.get(i - 1)),
                    "Time " + i + " should be after time " + (i - 1));
    }

    @Test
    void useCase2TotalTime() {
        LocalDateTime dep = LocalDateTime.of(2024, 9, 12, 6, 0);
        List<LocalDateTime> times = service.calculateArrivalTimes(
                Arrays.asList(Location.ASP, Location.ADL, Location.MEL, Location.SYD, Location.BRI), dep);
        long hours = Duration.between(dep, times.get(4)).toHours();
        assertTrue(hours >= 45 && hours <= 48, "Expected around 46h, is " + hours);
    }
}
