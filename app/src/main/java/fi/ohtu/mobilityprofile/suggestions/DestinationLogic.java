package fi.ohtu.mobilityprofile.suggestions;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.Place;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class DestinationLogic {
    private Place latestStartLocation;
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
     * Returns the most probable destination, when the user is in startLocation.
     *
     * @param startLocation SignificantPlace where the user is starting
     * @return Most probable destination
     */
    public String getMostLikelyDestination(Place startLocation) {
        List<String> destinations = getListOfMostLikelyDestinations(startLocation);
        return destinations.isEmpty() ? "Home" : destinations.get(0);
    }

    /**
     * Returns a list of most probable destinations, when the user is in startLocation.
     *
     * @param startLocation SignificantPlace where the user is starting
     * @return List of most probable destinations
     */
    public ArrayList<String> getListOfMostLikelyDestinations(Place startLocation) {
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
    public Place getLatestStartLocation() {
        return latestStartLocation;
    }
}
