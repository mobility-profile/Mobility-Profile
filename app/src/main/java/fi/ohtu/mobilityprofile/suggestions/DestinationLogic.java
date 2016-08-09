package fi.ohtu.mobilityprofile.suggestions;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GPSPoint;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class DestinationLogic {
    private GPSPoint latestStartLocation;
    private List<Suggestion> latestSuggestions;
    private List<SuggestionSource> suggestionSources;

    /**
     * Creates the MobilityProfile.
     *
     * @param suggestionSources sources of the suggestions
     */
    public DestinationLogic(List<SuggestionSource> suggestionSources) {
        this.suggestionSources = suggestionSources;
    }

    /**
     * Returns a list of most probable destinations, when the user is in startLocation.
     *
     * @param startLocation Place where the user is starting
     * @return List of most probable destinations
     */
    public ArrayList<String> getListOfMostLikelyDestinations(GPSPoint startLocation) {
        this.latestStartLocation = startLocation;

        List<Suggestion> suggestions = new ArrayList<>();
        for (SuggestionSource suggestionSource : suggestionSources) {
            suggestions.addAll(suggestionSource.getSuggestions(startLocation));
        }

        latestSuggestions = suggestions;

        ArrayList<String> destinations = new ArrayList<>();
        for (Suggestion suggestion : suggestions) {
            destinations.add(suggestion.getDestination());
        }

        return destinations;
    }

    /**
     * Returns a list of most probable destinations, when the user is in startLocation.
     *
     * @param startLocation Place where the user is starting
     * @return List of most probable destinations
     */
    public String getMostLikelyDestinations(GPSPoint startLocation) {
        this.latestStartLocation = startLocation;

        List<Suggestion> suggestions = new ArrayList<>();
        for (SuggestionSource suggestionSource : suggestionSources) {
            suggestions.addAll(suggestionSource.getSuggestions(startLocation));
        }

        latestSuggestions = suggestions;

        JSONArray destinations = new JSONArray();
        for (Suggestion suggestion : suggestions) {
            destinations.put(convertToGeojson(suggestion));
        }

        return destinations.toString();
    }

    /**
     * Returns a list of the latest destinations that were sent to the client.
     *
     * @return List of latest given destinations
     */
    public List<Suggestion> getLatestSuggestions() {
        return latestSuggestions;
    }

    /**
     * Returns the latest used start location.
     *
     * @return Latest start location
     */
    public GPSPoint getLatestStartLocation() {
        return latestStartLocation;
    }

    /**
     * Converts the suggestion to a GeoJSON object and then to a JSONObject.
     * @param suggestion suggestion to be converted
     * @return jsonObject
     */
    private JSONObject convertToGeojson(Suggestion suggestion) {

        JSONObject destination = new JSONObject();

        Feature feature = new Feature();
        if (suggestion.getCoordinate() != null) {
            feature.setGeometry(new Point(suggestion.getCoordinate().getLongitude(), suggestion.getCoordinate().getLatitude()));
        }
        try {
            feature.setProperties(new JSONObject().put("destination", suggestion.getDestination()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            destination = feature.toJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return destination;
    }
}
