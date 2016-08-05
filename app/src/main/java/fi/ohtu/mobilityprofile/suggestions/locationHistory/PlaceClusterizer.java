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

    static final double SPEED_LIMIT = 0.8;
    static final long TIME_SPENT_IN_CLUSTER_THRESHOLD = 600000; //10 minutes
    static final double WANDERING_DISTANCE_LIMIT = 70;
    static final double CLUSTER_RADIUS = 100;

    public PlaceClusterizer(Context context) {
        this.context = context;
    }

    public void updateVisitHistory(List<Place> places) {
        List<Cluster> clusters = formClusters(places);
        PlaceDao.deleteAllData();
        for (Cluster cluster : clusters) {
            if (cluster.hasInsufficientData()) {
                for(Place place : cluster.getPlaces()) {
                    PlaceDao.insert(place);
                }
            } else {
                createVisit(cluster);
            }
        }
    }

    private List<Cluster> formClusters(List<Place> places) {
        List<Cluster> clusters = new ArrayList<>();
        List<Place> placesToCheck = new ArrayList<>(places);
        for (int i = 0; i < places.size(); i++) {
            if (placesToCheck.contains(places.get(i))) {
                Cluster cluster = new Cluster();
                while (i < places.size() - 1 && speedBetweenPlaces(places.get(i), places.get(i + 1)) < SPEED_LIMIT) {
                    cluster.add(places.get(i));
                    i++;
                    if (cluster.timeSpent() > TIME_SPENT_IN_CLUSTER_THRESHOLD && places.get(i).distanceTo(places.get(i + 1)) > WANDERING_DISTANCE_LIMIT) {
                        break;
                    }
                }
                cluster.add(places.get(i));
                if (i == places.size() - 1) {
                    cluster.setInsufficientData(true);
                }
                if (cluster.timeSpent() > TIME_SPENT_IN_CLUSTER_THRESHOLD) {
                    clusters.add(cluster);
                    placesToCheck.removeAll(findPlacesWithinDistance(cluster.centerCoordinate(), places, CLUSTER_RADIUS));
                }
            }
        }
        return clusters;
    }

    private boolean createVisit(Cluster cluster) {
        SignificantPlace closestSignificantPlace = SignificantPlaceDao.getSignificantPlaceClosestTo(cluster.centerCoordinate());
        if (closestSignificantPlace == null || closestSignificantPlace.getCoordinate().distanceTo(cluster.centerCoordinate()) > CLUSTER_RADIUS) {
            Visit visit = new Visit(cluster.get(0).getTimestamp(), cluster.get(cluster.size() - 1).getTimestamp(), createSignificantPlace(cluster.centerCoordinate()));
            VisitDao.insert(visit);
            return true;
        } else if (!VisitDao.getLast().getSignificantPlace().equals(closestSignificantPlace)) {
            Visit visit = new Visit(cluster.get(0).getTimestamp(), cluster.get(cluster.size() - 1).getTimestamp(), closestSignificantPlace);
            VisitDao.insert(visit);
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
