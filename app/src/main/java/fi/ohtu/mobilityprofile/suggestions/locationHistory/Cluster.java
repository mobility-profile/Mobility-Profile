package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;

/**
 *
 */
public class Cluster {
    private List<Place> places;
    private boolean insufficientData;

    public Cluster() {
        places = new ArrayList<>();
        insufficientData = false;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void add(Place place) {
        places.add(place);
    }

    public Place get(int index) {
        return places.get(index);
    }

    public int size() {
        return places.size();
    }

    public void setInsufficientData(boolean value) {
        insufficientData = value;
    }

    public boolean hasInsufficientData() {
        return insufficientData;
    }

    public long timeSpent() {
        Collections.sort(places);
        return places.size() > 0 ? places.get(places.size() - 1).getTimestamp() - places.get(0).getTimestamp() : 0;
    }

    public Coordinate centerCoordinate() {
        float lat = 0;
        float lon = 0;
        for (Place place : places) {
            lat += place.getLatitude();
            lon += place.getLongitude();
        }
        lat /= places.size();
        lon /= places.size();
        return new Coordinate(lat, lon);
    }
}
