package fi.ohtu.mobilityprofile.suggestions;

import java.util.List;

import fi.ohtu.mobilityprofile.domain.GPSPoint;

/**
 * Interface for different route suggestion sources.
 */
public interface SuggestionSource {
    int CALENDAR_SUGGESTION = 1;
    int PLACE_SUGGESTIONS = 2;
    int ROUTE_SUGGESTION = 3;
    int FAVORITE_SUGGESTION = 4;
    int VISIT_SUGGESTIONS = 5;

    /**
     * Returns a list of most probable destinations, based on the data the implementing class
     * uses.
     *
     * @param startLocation Starting location
     * @return List of probable destinations
     */
    List<Suggestion> getSuggestions(GPSPoint startLocation);
}
