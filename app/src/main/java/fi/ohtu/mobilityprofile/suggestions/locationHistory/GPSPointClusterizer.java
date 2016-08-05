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

    public void updateVisitHistory(List<GPSPoint> GPSPoints) {
        List<Cluster> clusters = formClusters(GPSPoints);
        GPSPointDao.deleteAllData();
        for (Cluster cluster : clusters) {
            if (cluster.hasInsufficientData()) {
                for(GPSPoint GPSPoint : cluster.getGPSPoints()) {
                    GPSPointDao.insert(GPSPoint);
                }
            } else {
                createVisit(cluster);
            }
        }
    }

    private List<Cluster> formClusters(List<GPSPoint> GPSPoints) {
        List<Cluster> clusters = new ArrayList<>();
        List<GPSPoint> pointsToCheck = new ArrayList<>(GPSPoints);
        for (int i = 0; i < GPSPoints.size(); i++) {
            if (pointsToCheck.contains(GPSPoints.get(i))) {
                Cluster cluster = new Cluster();
                while (i < GPSPoints.size() - 1 && speedBetweenPlaces(GPSPoints.get(i), GPSPoints.get(i + 1)) < SPEED_LIMIT) {
                    cluster.add(GPSPoints.get(i));
                    i++;
                    if (cluster.timeSpent() > TIME_SPENT_IN_CLUSTER_THRESHOLD && GPSPoints.get(i).distanceTo(GPSPoints.get(i + 1)) > WANDERING_DISTANCE_LIMIT) {
                        break;
                    }
                }
                cluster.add(GPSPoints.get(i));
                if (i == GPSPoints.size() - 1) {
                    cluster.setInsufficientData(true);
                }
                if (cluster.timeSpent() > TIME_SPENT_IN_CLUSTER_THRESHOLD) {
                    clusters.add(cluster);
                    pointsToCheck.removeAll(findPlacesWithinDistance(cluster.centerCoordinate(), GPSPoints, CLUSTER_RADIUS));
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

    private double speedBetweenPlaces(GPSPoint GPSPoint1, GPSPoint GPSPoint2) {
        double distance = GPSPoint1.distanceTo(GPSPoint2);
        return distance / ((GPSPoint2.getTimestamp() / 1000) - (GPSPoint1.getTimestamp() / 1000));
    }

    private List<GPSPoint> findPlacesWithinDistance(Coordinate origin, List<GPSPoint> GPSPoints, double distanceLimit) {
        ArrayList<GPSPoint> placesWithinDistance = new ArrayList<>();
        for (GPSPoint GPSPoint : GPSPoints) {
            if (GPSPoint.distanceTo(origin) < distanceLimit) {
                placesWithinDistance.add(GPSPoint);
            }
        }
        return placesWithinDistance;
    }
}
