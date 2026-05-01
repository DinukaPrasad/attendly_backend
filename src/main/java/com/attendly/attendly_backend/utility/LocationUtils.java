package com.attendly.attendly_backend.utility;

/**
 * Utility class providing location-based calculations using the Haversine formula.
 */
public final class LocationUtils {

    private static final double EARTH_RADIUS_METERS = 6_371_000.0;

    private LocationUtils() {}

    /**
     * Calculates the great-circle distance between two points on Earth using the Haversine formula.
     *
     * @param lat1 latitude of the first point in decimal degrees
     * @param lon1 longitude of the first point in decimal degrees
     * @param lat2 latitude of the second point in decimal degrees
     * @param lon2 longitude of the second point in decimal degrees
     * @return the distance between the two points in metres
     */
    public static double calculateDistance(
            double lat1, double lon1,
            double lat2, double lon2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }

    /**
     * Determines whether two geographic points are within a specified distance of each other.
     *
     * @param lat1            latitude of the first point in decimal degrees
     * @param lon1            longitude of the first point in decimal degrees
     * @param lat2            latitude of the second point in decimal degrees
     * @param lon2            longitude of the second point in decimal degrees
     * @param thresholdMeters the maximum allowed distance in metres (inclusive)
     * @return {@code true} if the distance between the two points is less than or equal to
     *         {@code thresholdMeters}; {@code false} otherwise
     */
    public static boolean isWithinRange(
            double lat1, double lon1,
            double lat2, double lon2,
            double thresholdMeters) {

        return calculateDistance(lat1, lon1, lat2, lon2) <= thresholdMeters;
    }
}