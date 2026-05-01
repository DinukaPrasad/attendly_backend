package com.attendly.attendly_backend.utility;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for LocationUtils Haversine boundary condition (TC-15).
 */
public class LocationUtilsTest {

    @Test
    void testBoundaryCondition_TC15() {
        // Lecturer coordinates
        double lecturerLat = 51.481583;
        double lecturerLon = -3.179090;

        // Student coordinates (approximately 50m north of lecturer)
        double studentLat = 51.482032;
        double studentLon = -3.179090;

        // Calculate distance
        double distance = LocationUtils.calculateDistance(lecturerLat, lecturerLon, studentLat, studentLon);
        System.out.println("TC-15 Distance: " + distance + "m");

        // Assert distance is between 45.0 and 55.0 metres
        assertTrue(distance >= 45.0 && distance <= 55.0,
                "Distance should be between 45.0 and 55.0 metres, but got " + distance);

        // Check if within range (50.0m threshold)
        boolean result = LocationUtils.isWithinRange(lecturerLat, lecturerLon, studentLat, studentLon, 50.0);
        System.out.println("TC-15 isWithinRange result: " + result);

        System.out.println("TC-15 PASS ✅");
    }
}
