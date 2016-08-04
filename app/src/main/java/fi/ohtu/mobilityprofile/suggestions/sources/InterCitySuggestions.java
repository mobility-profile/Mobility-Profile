package fi.ohtu.mobilityprofile.suggestions.sources;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.InterCitySearchDao;
import fi.ohtu.mobilityprofile.domain.InterCitySearch;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionAccuracy;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

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
    public List<Suggestion> getSuggestions(String startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        int counter = 0;
        for (InterCitySearch search : InterCitySearchDao.getAllSearches()) {
            suggestions.add(new Suggestion(search.getDestination(), SuggestionAccuracy.MODERATE, INTER_CITY_SUGGESTION));
            counter++;
            if (counter == 5) break;
        }

        return suggestions;
    }
}
