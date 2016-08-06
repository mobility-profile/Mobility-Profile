package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.GPSPointDao;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GPSPoint;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.Visit;

/**
 * Class for clusterizing places into significant locations
 */
public class GPSPointClusterizer {

    private Context context;

    public static final double SPEED_LIMIT = 0.8;
    public static final long TIME_SPENT_IN_CLUSTER_THRESHOLD = 600000; //10 minutes
    public static final double WANDERING_DISTANCE_LIMIT = 70;
    public static final double CLUSTER_RADIUS = 100;

    public GPSPointClusterizer(Context context) {
        this.context = context;
    }

    public void updateVisitHistory(List<GPSPoint> gpsPoints) {
        List<Cluster> clusters = formClusters(gpsPoints);
        GPSPointDao.deleteAllData();
        for (Cluster cluster : clusters) {
            if (cluster.hasInsufficientData()) {
                for(GPSPoint gpsPoint : cluster.getGPSPoints()) {
                    GPSPointDao.insert(gpsPoint);
                }
            } else {
                createVisit(cluster);
            }
        }
    }

    private List<Cluster> formClusters(List<GPSPoint> gpsPoints) {
        List<Cluster> clusters = new ArrayList<>();
        List<GPSPoint> pointsToCheck = new ArrayList<>(gpsPoints);
        for (int i = 0; i < gpsPoints.size(); i++) {
            if (pointsToCheck.contains(gpsPoints.get(i))) {
                Cluster cluster = new Cluster();
                while (i < gpsPoints.size() - 1 && speedBetweenPlaces(gpsPoints.get(i), gpsPoints.get(i + 1)) < SPEED_LIMIT) {
                    cluster.add(gpsPoints.get(i));
                    if (cluster.timeSpent() > TIME_SPENT_IN_CLUSTER_THRESHOLD && gpsPoints.get(i).distanceTo(gpsPoints.get(i + 1)) > WANDERING_DISTANCE_LIMIT) {
                        break;
                    }
                    i++;
                }
                cluster.add(gpsPoints.get(i));
                if (i == gpsPoints.size() - 1) {
                    cluster.setInsufficientData(true);
                    clusters.add(cluster);
                }
                else if (cluster.timeSpent() > TIME_SPENT_IN_CLUSTER_THRESHOLD) {
                    clusters.add(cluster);
                    pointsToCheck.removeAll(findPlacesWithinDistance(cluster.centerCoordinate(), gpsPoints, CLUSTER_RADIUS));
                }
            }
        }
        return clusters;
    }

    private boolean createVisit(Cluster cluster) {
        Place closestPlace = PlaceDao.getPlaceClosestTo(cluster.centerCoordinate());
        if (closestPlace == null || closestPlace.distanceTo(cluster.centerCoordinate()) > CLUSTER_RADIUS) {
            Visit visit = new Visit(cluster.get(0).getTimestamp(), cluster.get(cluster.size() - 1).getTimestamp(), createPlace(cluster.centerCoordinate()));
            VisitDao.insert(visit);
            return true;
        } else if (VisitDao.getLast().getPlace() != null && !VisitDao.getLast().getPlace().equals(closestPlace)) {
            Visit visit = new Visit(cluster.get(0).getTimestamp(), cluster.get(cluster.size() - 1).getTimestamp(), closestPlace);
            VisitDao.insert(visit);
            return true;
        }
        return false;
    }

    private Place createPlace(Coordinate coordinate) {
        Place place = new Place("name", "address", coordinate);
        PlaceDao.insertPlace(place);
        AddressConverter.getAddressAndSave(place, context);
        place.setName(place.getAddress());
        PlaceDao.insertPlace(place);
        return place;
    }

    private double speedBetweenPlaces(GPSPoint gpsPoint1, GPSPoint gpsPoint2) {
        double distance = gpsPoint1.distanceTo(gpsPoint2);
        return distance / ((gpsPoint2.getTimestamp() / 1000) - (gpsPoint1.getTimestamp() / 1000));
    }

    private List<GPSPoint> findPlacesWithinDistance(Coordinate origin, List<GPSPoint> gpsPoints, double distanceLimit) {
        ArrayList<GPSPoint> placesWithinDistance = new ArrayList<>();
        for (GPSPoint gpsPoint : gpsPoints) {
            if (gpsPoint.distanceTo(origin) < distanceLimit) {
                placesWithinDistance.add(gpsPoint);
            }
        }
        return placesWithinDistance;
    }
}
