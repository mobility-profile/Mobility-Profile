package fi.ohtu.mobilityprofile.suggestions.sources;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.InterCitySearchDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.InterCitySearch;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionAccuracy;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;
import fi.ohtu.mobilityprofile.util.AddressConverter;

/**
 * This class creates suggestions based on InterCitySearches the user has made
 */
public class InterCitySuggestions implements SuggestionSource {

    /**
     * Returns a list of probable locations the user would like to visit based on the previous
     * searches the user has made. This method is valid for inter city travelling only.
     *
     * @param startLocation Starting location
     * @return List of probable destinations
     *
     */
    @Override
    public List<Suggestion> getSuggestions(StartLocation startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        for (RouteSearch search : RouteSearchDao.getAll(DestinationLogic.MODE_INTERCITY)) {
            if (search.getStartlocation().equals(startLocation)) {
                suggestions.add(new Suggestion(search.getDestination(), SuggestionAccuracy.MODERATE, INTER_CITY_SUGGESTION));
            }
            if (suggestions.size() >= 5) {
                break;
            }
        }

        return suggestions;
    }
}
