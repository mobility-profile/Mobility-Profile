package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GpsPoint;

/**
 *
 */
public class Cluster {
    private List<GpsPoint> gpsPoints;
    private boolean insufficientData;

    public Cluster() {
        gpsPoints = new ArrayList<>();
        insufficientData = false;
    }

    public List<GpsPoint> getGpsPoints() {
        return gpsPoints;
    }

    public void add(GpsPoint gpsPoint) {
        gpsPoints.add(gpsPoint);
    }

    public GpsPoint get(int index) {
        return gpsPoints.get(index);
    }

    public int size() {
        return gpsPoints.size();
    }

    public void setInsufficientData(boolean value) {
        insufficientData = value;
    }

    public boolean hasInsufficientData() {
        return insufficientData;
    }

    public long timeSpent() {
        Collections.sort(gpsPoints);
        return gpsPoints.size() > 0 ? gpsPoints.get(gpsPoints.size() - 1).getTimestamp() - gpsPoints.get(0).getTimestamp() : 0;
    }

    public Coordinate centerCoordinate() {
        float lat = 0;
        float lon = 0;
        for (GpsPoint GpsPoint : gpsPoints) {
            lat += GpsPoint.getLatitude();
            lon += GpsPoint.getLongitude();
        }
        lat /= gpsPoints.size();
        lon /= gpsPoints.size();
        return new Coordinate(lat, lon);
    }
}
