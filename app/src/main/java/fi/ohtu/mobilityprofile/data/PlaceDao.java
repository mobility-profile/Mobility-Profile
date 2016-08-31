package fi.ohtu.mobilityprofile.data;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.Point;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;

/**
 * DAO used for saving and reading Places to/from the database.
 */
public class PlaceDao {

    /**
     * Returns a Place closest to given coordinates
     * @param coordinate coordinates of the given location
     * @return Place
     */
    public static Place getPlaceClosestTo(Coordinate coordinate) {
        List<Place> places = getAll();
        Place result = null;
        double distance = Double.MAX_VALUE;
        for(Place place : places) {
            if(place.distanceTo(coordinate) < distance){
                distance = place.distanceTo(coordinate);
                result = place;
            }
        }
        return result;
    }

    public static List<Place> getAll() {
        return Place.listAll(Place.class);
    }


    /**
     * Saves a Place to the database.
     * @param place Place to be saved
     */
    public static void insertPlace(Place place) {
        place.getCoordinate().save();
        place.save();
    }

    /**
     * Finds a Place by id
     * @param id id of the Place
     * @return Place with the given id
     */

    public static Place getPlaceById(Long id) {
        List<Place> places = Select.from(Place.class)
                .where(Condition.prop("id").eq(id))
                .limit("1")
                .list();

        assert places.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        return  (places.size() == 1) ? places.get(0) : null;
    }

    /**
     * Finds a Place by name
     * @param name name of the Place
     * @return Place with the given name
     */

    public static Place getPlaceByName(String name) {
        List<Place> places = Select.from(Place.class)
                .where(Condition.prop("name").eq(name))
                .limit("1")
                .list();

        assert places.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        return  (places.size() == 1) ? places.get(0) : null;
    }

    /**
     * Deletes a Place by id
     * @param id id of the Place to be deleted
     */
    public static void deletePlaceById(Long id) {
        Place place = getPlaceById(id);
        if (place != null) {
            place.delete();
        }
    }

    /**
     * Returns places with favourite = true
     * @return list of all favourited Places
     */
    public static List<Place> getFavouritePlaces() {
        List<Place> allPlaces = Place.listAll(Place.class);
        List<Place> remove = new ArrayList<>();

        for (Place place: allPlaces) {
            if (!place.isFavourite()) {
                remove.add(place);
            }
        }
        allPlaces.removeAll(remove);

        return allPlaces;
    }

    /**
     * Returns places with favourite = true
     * @return JSONArray of favourited places in String
     */
    public static String getFavouritePlacesInJson() {
        List<Place> allPlaces = Place.listAll(Place.class);

        JSONArray favourites = new JSONArray();

        for (Place place: allPlaces) {
            if (place.isFavourite()) {

                JSONObject favourite = new JSONObject();

                Feature feature = new Feature();
                if (place.getCoordinate() != null) {
                    feature.setGeometry(new Point(place.getCoordinate().getLongitude(), place.getCoordinate().getLatitude()));
                }
                try {
                    feature.setProperties(new JSONObject().put("label", place.getAddress()).put("layer", "mobilityprofile"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    favourite = feature.toJSON();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                favourites.put(favourite);

            }
        }

        return favourites.toString();
    }

    /**
     * Deletes all Place data from the database
     */
    public static void deleteAllData() {
        Place.deleteAll(Place.class);
    }
}
