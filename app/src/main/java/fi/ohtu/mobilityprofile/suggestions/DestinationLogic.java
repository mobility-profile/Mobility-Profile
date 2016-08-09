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
import fi.ohtu.mobilityprofile.suggestions.sources.InterCitySuggestions;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class DestinationLogic {
    public static final int INTRA_CITY_SUGGESTION = 1;
    public static final int INTER_CITY_SUGGESTION = 2;

    private GPSPoint latestStartLocation;
    private List<Suggestion> latestSuggestions;
    private List<SuggestionSource> suggestionSources;

    private SuggestionSource interCitySuggestions;

    private int latestSuggestionType;

    /**
     * Creates the MobilityProfile.
     *
     * @param suggestionSources sources of the suggestions
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
    public String getListOfIntraCitySuggestions(GPSPoint startLocation) {
        this.latestStartLocation = startLocation;
        this.latestSuggestionType = INTRA_CITY_SUGGESTION;

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
    public String getListOfInterCitySuggestions(GPSPoint startLocation) {
        this.latestStartLocation = startLocation;
        this.latestSuggestionType = INTER_CITY_SUGGESTION;

        List<Suggestion> suggestions = new ArrayList<>();
        suggestions.addAll(interCitySuggestions.getSuggestions(startLocation));

        return getDestinations(suggestions);
    }

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
            feature.setProperties(new JSONObject().put("label", suggestion.getDestination()).put("layer", "mobilityprofile"));
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

    /*
 * Returns the type of the latest suggestion.
 *
 * @return Type of the latest suggestion
 */
    public int getLatestSuggestionType() {
        return latestSuggestionType;
    }
}
