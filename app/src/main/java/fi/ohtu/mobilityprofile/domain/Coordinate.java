package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class for saving coordinates to be used by any class needing them
 */
public class Coordinate extends SugarRecord {
    Float latitude;
    Float longitude;

    public Coordinate() {}

    /**
     * Creates Coordinate
     * @param latitude latitude of the coordinate
     * @param longitude longitude of the coordinate
     */
    public Coordinate(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public Float getLongitude() {
        return this.longitude;
    }

    /**
     * Calculates the distance between two coordinates.
     * @param coordinate coordinate which is compared to this coordinate
     * @return distance
     */
    public double distanceTo(Coordinate coordinate) {
        final int R = 6371; // Radius of the earth
        Double latDistance = Math.toRadians(coordinate.getLatitude() - this.latitude);
        Double lonDistance = Math.toRadians(coordinate.getLongitude() - this.longitude);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(coordinate.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        return latitude.equals(that.latitude) && longitude.equals(that.longitude);

    }

}
