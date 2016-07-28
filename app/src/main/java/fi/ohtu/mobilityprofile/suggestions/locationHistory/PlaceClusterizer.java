package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.Place;

/**
 * Class for clusterizing places into significant locations
 */
public class PlaceClusterizer {

    public static List<Place> clusterize(List<Place> places) {

        List<Place> significantPlaces = new ArrayList<>();
        List<Place> modifiedPlaces = new ArrayList<>(places);

        double speedLimit = 0.8;
        long timeSpentInClusterThreshold = 600000; //10 minutes
        double wanderingDistanceLimit = 70;
        double clusterRadius = 100;

        for(int i = 0; i < places.size() - 1; i++) {
            if(modifiedPlaces.contains(places.get(i)) && speedBetweenPlaces(places.get(i), places.get(i+1)) < speedLimit) {
                List<Place> cluster = new ArrayList<>();
                int j = i;
                long timeSpentInCluster = 0;
                while (j < places.size() - 1 && speedBetweenPlaces(places.get(j), places.get(j+1)) < speedLimit) {
                    cluster.add(places.get(j));
                    timeSpentInCluster += places.get(j+1).getTimestamp() - places.get(j).getTimestamp();
                    j++;
                    if(timeSpentInCluster > timeSpentInClusterThreshold && distance(places.get(j), places.get(j+1)) > wanderingDistanceLimit){
                        break;
                    }
                }
                if(timeSpentInCluster > timeSpentInClusterThreshold) {
                    cluster.add(places.get(j));
                    significantPlaces.add(mean(cluster));
                    modifiedPlaces.removeAll(findPlacesWithinDistance(mean(cluster), places, clusterRadius));
                }
            }
        }
        for(Place place : significantPlaces) {
            //System.out.println("SIGNIFICANT PLACE lat: " + place.getLatitude() + " lon: " + place.getLongitude());
            print(place, 0);
        }
        return significantPlaces;
    }

    private static double speedBetweenPlaces(Place place1, Place place2) {
        double distance = distance(place1, place2);
        return distance / ((place2.getTimestamp()/1000) - (place1.getTimestamp()/1000));
    }

    private static void print(Place place, int order){
        System.out.println("<trkpt lat=\"" + place.getLatitude() + "\" lon=\"" + place.getLongitude() +"\"><time>2016-07-21T"+order+":00:00Z</time><src>network</src></trkpt>");
    }

    public static double distance(Place place1, Place place2) {
        final int R = 6371; // Radius of the earth
        Double latDistance = Math.toRadians(place2.getLatitude() - place1.getLatitude());
        Double lonDistance = Math.toRadians(place2.getLongitude() - place1.getLongitude());
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(place1.getLatitude())) * Math.cos(Math.toRadians(place2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    public static Place mean(List<Place> places) {
        long time = 0;
        float lat = 0;
        float lon = 0;
        for(Place place : places) {
            time += place.getTimestamp();
            lat += place.getLatitude();
            lon += place.getLongitude();
        }
        time /= places.size();
        lat /= places.size();
        lon /= places.size();
        return new Place(0, lat, lon);
    }

    private static List<Place> findPlacesWithinDistance(Place origin, List<Place> places, double distanceLimit) {
        ArrayList<Place> placesWithinDistance = new ArrayList<Place>();
        for(Place place : places) {
            if((distance(origin, place) < distanceLimit)) {
                placesWithinDistance.add(place);
            }
        }
        return placesWithinDistance;
    }




}
