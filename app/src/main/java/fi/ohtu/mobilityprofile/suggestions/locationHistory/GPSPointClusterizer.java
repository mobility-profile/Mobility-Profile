package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.GPSPointDao;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GPSPoint;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;
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
                    i++;
                    if (cluster.timeSpent() > TIME_SPENT_IN_CLUSTER_THRESHOLD && gpsPoints.get(i).distanceTo(gpsPoints.get(i + 1)) > WANDERING_DISTANCE_LIMIT) {
                        break;
                    }
                }
                cluster.add(gpsPoints.get(i));
                if (i == gpsPoints.size() - 1) {
                    cluster.setInsufficientData(true);
                }
                if (cluster.timeSpent() > TIME_SPENT_IN_CLUSTER_THRESHOLD) {
                    clusters.add(cluster);
                    pointsToCheck.removeAll(findPlacesWithinDistance(cluster.centerCoordinate(), gpsPoints, CLUSTER_RADIUS));
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
