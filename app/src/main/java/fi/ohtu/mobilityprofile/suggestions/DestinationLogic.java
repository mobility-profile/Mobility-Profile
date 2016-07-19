package fi.ohtu.mobilityprofile.suggestions;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for calculating the most likely trips the user is going to make.
 */
public class DestinationLogic {
    private String latestStartLocation;
    private List<Suggestion> latestSuggestions;

    private List<SuggestionSource> suggestionSources;

    /**
     * Creates the MobilityProfile.
     *
     * @param suggestionSources
     */
    public DestinationLogic(List<SuggestionSource> suggestionSources) {
        this.suggestionSources = suggestionSources;
    }

    /**
     * Returns the most probable destination, when the user is in startLocation.
     *
     * The algorithm will first check if there are marked events in the calendar within a few hours.
     * If none was found, it will then check places where the user has previously visited.
     * If that data is not available, previously used searches are suggested.
     * Lastly, it will check saved favorite location and suggest one of those.
     *
     * TODO: the algorithm shouldn't always take the first decent suggestion.
     * It should instead compare suggestions from all the different sources and take the best
     * one of those.
     *
     * @param startLocation Location where the user is starting
     * @return Most probable destination
     */
    public String getMostLikelyDestination(String startLocation) {
        List<String> destinations = getListOfMostLikelyDestinations(startLocation);
        return destinations.isEmpty() ? "Home" : destinations.get(0);
    }

    /**
     * Returns a list of most probable destinations, when the user is in startLocation.
     *
     * The algorithm will first check if there are marked events in the calendar within a few hours.
     * It will then check previously used searches and places where the user has previously visited.
     * Lastly, it will check saved favorite locations and suggest one of those.
     *
     * TODO: the algorithm shouldn't always take the first decent suggestion.
     * It should instead compare suggestions from all the different sources and take the best
     * one of those.
     *
     * @param startLocation Location where the user is starting
     * @return List of most probable destinations
     */
    public ArrayList<String> getListOfMostLikelyDestinations(String startLocation) {
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
     * @return List of latest given destinations
     */
    public List<Suggestion> getLatestSuggestions() {
        return latestSuggestions;
    }

    public String getLatestStartLocation() {
        return latestStartLocation;
    }
}
