package fi.ohtu.mobilityprofile.suggestions;

import java.util.ArrayList;
import java.util.List;

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
    public ArrayList<String> getListOfIntraCitySuggestions(GPSPoint startLocation) {
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
    public ArrayList<String> getListOfInterCitySuggestions(GPSPoint startLocation) {
        this.latestStartLocation = startLocation;
        this.latestSuggestionType = INTER_CITY_SUGGESTION;

        List<Suggestion> suggestions = new ArrayList<>();
        suggestions.addAll(interCitySuggestions.getSuggestions(startLocation));

        return getDestinations(suggestions);
    }

    private ArrayList<String> getDestinations(List<Suggestion> suggestions) {
        latestSuggestions = suggestions;

        ArrayList<String> destinations = new ArrayList<>();
        for (Suggestion suggestion : suggestions) {
            destinations.add(suggestion.getDestination());
        }

        return destinations;
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
     * Returns the type of the latest suggestion.
     *
     * @return Type of the latest suggestion
     */
    public int getLatestSuggestionType() {
        return latestSuggestionType;
    }
}
