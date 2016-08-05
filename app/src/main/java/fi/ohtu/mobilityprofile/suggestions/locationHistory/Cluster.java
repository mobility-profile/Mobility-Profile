package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GPSPoint;

/**
 *
 */
public class Cluster {
    private List<GPSPoint> GPSPoints;
    private boolean insufficientData;

    public Cluster() {
        GPSPoints = new ArrayList<>();
        insufficientData = false;
    }

    public List<GPSPoint> getGPSPoints() {
        return GPSPoints;
    }

    public void add(GPSPoint GPSPoint) {
        GPSPoints.add(GPSPoint);
    }

    public GPSPoint get(int index) {
        return GPSPoints.get(index);
    }

    public int size() {
        return GPSPoints.size();
    }

    public void setInsufficientData(boolean value) {
        insufficientData = value;
    }

    public boolean hasInsufficientData() {
        return insufficientData;
    }

    public long timeSpent() {
        Collections.sort(GPSPoints);
        return GPSPoints.size() > 0 ? GPSPoints.get(GPSPoints.size() - 1).getTimestamp() - GPSPoints.get(0).getTimestamp() : 0;
    }

    public Coordinate centerCoordinate() {
        float lat = 0;
        float lon = 0;
        for (GPSPoint GPSPoint : GPSPoints) {
            lat += GPSPoint.getLatitude();
            lon += GPSPoint.getLongitude();
        }
        lat /= GPSPoints.size();
        lon /= GPSPoints.size();
        return new Coordinate(lat, lon);
    }
}
