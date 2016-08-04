package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;
import fi.ohtu.mobilityprofile.domain.Visit;

/**
 * Class for clusterizing places into significant locations
 */
public class PlaceClusterizer {

    private Context context;
    private PlaceDao placeDao;
    private SignificantPlaceDao significantPlaceDao;
    private VisitDao visitDao;

    static final double SPEED_LIMIT = 0.8;
    static final long TIME_SPENT_IN_CLUSTER_THRESHOLD = 600000; //10 minutes
    static final double WANDERING_DISTANCE_LIMIT = 70;
    static final double CLUSTER_RADIUS = 100;

    public PlaceClusterizer(Context context) {
        this.context = context;
        this.placeDao = new PlaceDao();
        this.significantPlaceDao = new SignificantPlaceDao();
        this.visitDao = new VisitDao();
    }

    public void updateVisitHistory(List<Place> places) {
        List<List<Place>> clusters = formClusters(places);
        for (List<Place> cluster : clusters) {
            createVisit(cluster);
        }
    }

    private long timeSpentIn(List<Place> cluster) {
        return cluster.get(cluster.size() - 1).getTimestamp() - cluster.get(0).getTimestamp();
    }

    private List<List<Place>> formClusters(List<Place> places) {
        List<List<Place>> clusters = new ArrayList<>();
        List<Place> placesToCheck = new ArrayList<>(places);
        for (int i = 0; i < places.size(); i++) {
            if (placesToCheck.contains(places.get(i))) {
                List<Place> cluster = new ArrayList<>();
                long timeSpentInCluster = 0;
                while (i < places.size() - 1 && speedBetweenPlaces(places.get(i), places.get(i + 1)) < SPEED_LIMIT) {
                    cluster.add(places.get(i));
                    timeSpentInCluster += places.get(i + 1).getTimestamp() - places.get(i).getTimestamp();
                    i++;
                    if (timeSpentInCluster > TIME_SPENT_IN_CLUSTER_THRESHOLD && places.get(i).distanceTo(places.get(i + 1)) > WANDERING_DISTANCE_LIMIT) {
                        break;
                    }
                }
                cluster.add(places.get(i));
                if (timeSpentIn(cluster) > TIME_SPENT_IN_CLUSTER_THRESHOLD) {
                    clusters.add(cluster);
                    placesToCheck.removeAll(findPlacesWithinDistance(meanCoordinate(cluster), places, CLUSTER_RADIUS));
                }
            }
        }
        return clusters;
    }

    private boolean createVisit(List<Place> cluster) {
        SignificantPlace closestSignificantPlace = significantPlaceDao.getSignificantPlaceClosestTo(meanCoordinate(cluster));
        if (closestSignificantPlace == null || closestSignificantPlace.getCoordinate().distanceTo(meanCoordinate(cluster)) > CLUSTER_RADIUS) {
            Visit visit = new Visit(cluster.get(0).getTimestamp(), cluster.get(cluster.size() - 1).getTimestamp(), createSignificantPlace(meanCoordinate(cluster)));
            visitDao.insert(visit);
            return true;
        } else if (!visitDao.getLast().getSignificantPlace().equals(closestSignificantPlace)) {
            Visit visit = new Visit(cluster.get(0).getTimestamp(), cluster.get(cluster.size() - 1).getTimestamp(), closestSignificantPlace);
            visitDao.insert(visit);
            return true;
        }
        return false;
    }

    private SignificantPlace createSignificantPlace(Coordinate coordinate) {
        SignificantPlace significantPlace = new SignificantPlace("name", "address", coordinate);
        SignificantPlaceDao.insertSignificantPlace(significantPlace);
        AddressConverter.getAddressAndSave(significantPlace, context);
        significantPlace.setName(significantPlace.getAddress());
        significantPlace.save();
        return significantPlace;
    }

    private double speedBetweenPlaces(Place place1, Place place2) {
        double distance = place1.distanceTo(place2);
        return distance / ((place2.getTimestamp() / 1000) - (place1.getTimestamp() / 1000));
    }

    private Coordinate meanCoordinate(List<Place> places) {
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

    private List<Place> findPlacesWithinDistance(Coordinate origin, List<Place> places, double distanceLimit) {
        ArrayList<Place> placesWithinDistance = new ArrayList<>();
        for (Place place : places) {
            if (place.distanceTo(origin) < distanceLimit) {
                placesWithinDistance.add(place);
            }
        }
        return placesWithinDistance;
    }
}
