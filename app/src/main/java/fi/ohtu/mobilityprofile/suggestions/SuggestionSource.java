package fi.ohtu.mobilityprofile.suggestions;

import java.util.List;

/**
 * Interface for different route suggestion sources.
 */
public interface SuggestionSource {
    int CALENDAR_SUGGESTION = 1;
    int VISIT_SUGGESTION = 2;
    int ROUTE_SUGGESTION = 3;
    int FAVORITE_SUGGESTION = 4;

    /**
     * Returns a list of most probable destinations, based on the data the implementing class
     * uses.
     *
     * @param startLocation Starting location
     * @return List of probable destinations
     */
    List<Suggestion> getSuggestions(String startLocation);
}
