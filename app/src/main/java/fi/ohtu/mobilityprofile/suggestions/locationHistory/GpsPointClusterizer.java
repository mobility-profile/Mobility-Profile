package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.GpsPointDao;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.Visit;
import fi.ohtu.mobilityprofile.util.geocoding.AddressConvertListener;
import fi.ohtu.mobilityprofile.util.geocoding.AddressConverter;

/**
 * Class for clustering places into significant locations
 */
public class GpsPointClusterizer {

    private Context context;

    public static final double SPEED_LIMIT = 0.8;
    public static final long TIME_SPENT_IN_CLUSTER_THRESHOLD = 10 * 60 * 1000; //10 minutes
    public static final double CLUSTER_RADIUS = 100;

    /**
     * Creates GpsPointClusterizer.
     * @param context context
     */
    public GpsPointClusterizer(Context context) {
        this.context = context;
    }

    /**
     * Updates visit history by forming clusters, deleting GpsPoints and creating Visits to
     * the Clusters if they have sufficient data. If not, GpsPoints of the Clusters with insufficient
     * data are inserted to the database again.
     * @param gpsPoints list of gpsPoints
     */
    public void updateVisitHistory(List<GpsPoint> gpsPoints) {
        List<Cluster> clusters = formClusters(gpsPoints);
        GpsPointDao.deleteAllData();
        for (Cluster cluster : clusters) {
            if (cluster.hasInsufficientData()) {
                for(GpsPoint gpsPoint : cluster.getGpsPoints()) {
                    GpsPointDao.insert(gpsPoint);
                }
            } else {
                createVisit(cluster);
            }
        }
    }

    /**
     * Forms clusters from the given list of GpsPoints
     * @param gpsPoints list of GpsPoints
     * @return list of clusters
     */
    private List<Cluster> formClusters(List<GpsPoint> gpsPoints) {
        List<Cluster> clusters = new ArrayList<>();
        List<GpsPoint> pointsToCheck = new ArrayList<>(gpsPoints);
        for (int i = 0; i < gpsPoints.size(); i++) {
            if (pointsToCheck.contains(gpsPoints.get(i))) {
                Cluster cluster = new Cluster();
                while (i < gpsPoints.size() - 1 && speedBetweenPlaces(gpsPoints.get(i), gpsPoints.get(i + 1)) < SPEED_LIMIT) {
                    cluster.add(gpsPoints.get(i));
                    if (cluster.timeSpent() > TIME_SPENT_IN_CLUSTER_THRESHOLD) {
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

    /**
     * Creates a Visit to the given Cluster and inserts it to the database.
     * @param cluster Cluster
     * @return true if Visit was created, false if not
     */
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
        } else if (VisitDao.getLast().getPlace() != null && VisitDao.getLast().getPlace().equals(closestPlace)) {
            Visit visit = VisitDao.getLast();
            visit.setExitTime(cluster.get(cluster.size() - 1).getTimestamp());
            VisitDao.insert(visit);
            return true;
        }
        return false;
    }

    /**
     * Creates a Place with the given coordinates
     * @param coordinate coordinates of the Place to be created
     * @return Place
     */
    private Place createPlace(Coordinate coordinate) {
        final Place place = new Place("name", "address", coordinate);
        PlaceDao.insertPlace(place);

        AddressConverter.convertToAddress(context, coordinate, new AddressConvertListener() {
            @Override
            public void addressConverted(String address, Coordinate coordinate) {
                place.setAddress(address);
                place.setName(address);
                PlaceDao.insertPlace(place);
            }
        });

        return place;
    }

    /**
     * Returns the speed the user moved between two GpsPoints.
     * @param gpsPoint1 the first GpsPoint
     * @param gpsPoint2 the second GpsPoint
     * @return speed of the transition
     */
    private double speedBetweenPlaces(GpsPoint gpsPoint1, GpsPoint gpsPoint2) {
        double distance = Math.max(gpsPoint1.distanceTo(gpsPoint2.getCoordinate()) - gpsPoint1.getAccuracy() - gpsPoint2.getAccuracy(), 0);
        return distance / ((gpsPoint2.getTimestamp() / 1000) - (gpsPoint1.getTimestamp() / 1000));
    }

    /**
     * Returns a list of GpsPoints that lie within a given distance limit from the given coordinates
     * @param origin the coordinate which is compared to GpsPoints
     * @param gpsPoints list of GpsPoints
     * @param distanceLimit distance limit from the origin coordinates
     * @return list of GpsPoints
     */
    private List<GpsPoint> findPlacesWithinDistance(Coordinate origin, List<GpsPoint> gpsPoints, double distanceLimit) {
        ArrayList<GpsPoint> placesWithinDistance = new ArrayList<>();
        for (GpsPoint gpsPoint : gpsPoints) {
            if (gpsPoint.distanceTo(origin) < distanceLimit) {
                placesWithinDistance.add(gpsPoint);
            }
        }
        return placesWithinDistance;
    }
}
