package fi.ohtu.mobilityprofile.suggestions;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.sources.InterCitySuggestions;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class DestinationLogic {
    public static final int MODE_INTRACITY = 300;
    public static final int MODE_INTERCITY = 301;

    private StartLocation latestStartLocation;
    private List<Suggestion> latestSuggestions;
    private List<SuggestionSource> suggestionSources;

    private SuggestionSource interCitySuggestions;

    private int latestSuggestionType;

    /**
     * Creates the DestinationLogic.
     *
     * @param suggestionSources sources of the suggestions
     * @param interCitySuggestions interCitySuggestions
     */
    public DestinationLogic(List<SuggestionSource> suggestionSources, InterCitySuggestions interCitySuggestions) {
        this.suggestionSources = suggestionSources;
        this.interCitySuggestions = interCitySuggestions;
    }

    /**
     * Returns a list of most probable destinations when the user is in startLocation.
     * (Inside a city)
     *
     * @param startLocation Place where the user is starting
     * @return List of most probable destinations
     */
    public String getListOfIntraCitySuggestions(StartLocation startLocation) {
        this.latestStartLocation = startLocation;
        this.latestSuggestionType = MODE_INTRACITY;

        List<Suggestion> suggestions = new ArrayList<>();
        for (SuggestionSource suggestionSource : suggestionSources) {
            suggestions.addAll(suggestionSource.getSuggestions(startLocation));
        }

        return getDestinations(suggestions);
    }

    /**
     * Returns a list of most probable destinations when the user is in startLocation.
     * (Between cities)
     *
     * @param startLocation Location where the user is starting
     * @return List of most probable destinations
     */
    public String getListOfInterCitySuggestions(StartLocation startLocation) {
        this.latestStartLocation = startLocation;
        this.latestSuggestionType = MODE_INTERCITY;

        List<Suggestion> suggestions = new ArrayList<>();
        suggestions.addAll(interCitySuggestions.getSuggestions(startLocation));

        return getDestinations(suggestions);
    }

    /**
     * Returns a JsonArray of destinations.
     * @param suggestions list of suggestions
     * @return JsonArray of destinations
     */
    private String getDestinations(List<Suggestion> suggestions) {
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
    public StartLocation getLatestStartLocation() {
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
        if (suggestion.getDestination().getCoordinate() != null) {
            feature.setGeometry(new Point(suggestion.getDestination().getCoordinate().getLatitude(),suggestion.getDestination().getCoordinate().getLongitude()));
        }
        try {
            feature.setProperties(new JSONObject().put("label", suggestion.getDestination()).put("layer", "mobilityprofile").put("name", suggestion.getDestination().getName()));
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

   /**
    * Returns the type of the latest suggestion.
    *
    * @return Type of the latest suggestion
    */
    public int getLatestSuggestionType() {
        return latestSuggestionType;
    }
}
