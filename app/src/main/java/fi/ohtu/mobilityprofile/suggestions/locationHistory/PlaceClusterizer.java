package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;

/**
 * Created by jussiviinikka on 21/07/16.
 */
public class PlaceClusterizer {

    public static List<Place> clusterize(List<Place> places) {
        List<Place> significantPlaces = new ArrayList<>();
        List<Place> modifiedPlaces = new ArrayList<>(places);
        int distanceLimit = 500;
        long timeLimit = 600000; //10 minutes;
        for(Place place : places) {
            if(modifiedPlaces.contains(place)) {
                Place origin = place;
                while(distance(origin, mean(findPlacesWithinDistanceAndTime(place, modifiedPlaces, distanceLimit, timeLimit))) > 50) {
                    origin = mean(findPlacesWithinDistanceAndTime(place, modifiedPlaces, distanceLimit, timeLimit));
                }
                significantPlaces.add(origin);
                modifiedPlaces.removeAll(findPlacesWithinDistanceAndTime(origin, modifiedPlaces, distanceLimit, timeLimit));
            }
        }
        for(Place place : significantPlaces) {
            System.out.println("SIGNIFICANT PLACE lat: " + place.getLatitude() + " lon: " + place.getLongitude());
        }
        return significantPlaces;
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
        return new Place(time, lat, lon);
    }

    private static List<Place> findPlacesWithinDistanceAndTime(Place origin, List<Place> places, double distanceLimit, long timeLimit) {
        ArrayList<Place> placesWithinDistance = new ArrayList<Place>();
        for(Place place : places) {
            if((distance(origin, place) < distanceLimit) && (Math.abs(origin.getTimestamp() - place.getTimestamp()) < timeLimit)) {
                placesWithinDistance.add(place);
            }
        }
        return placesWithinDistance;
    }




}
