package fi.ohtu.mobilityprofile.suggestions.sources;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.domain.FavouritePlace;
import fi.ohtu.mobilityprofile.domain.GPSPoint;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionAccuracy;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

/**
 * This class creates suggestions based on favorites the user has saved.
 */
public class FavoriteSuggestions implements SuggestionSource {

    /**
     * Creates the FavoriteSuggestions
     */
    public FavoriteSuggestions() {
    }

    /**
     * Returns three most used favorite places.
     *
     * @param startLocation Location where the user starts the journey
     * @return Three favorite places
     */
    @Override
    public List<Suggestion> getSuggestions(GPSPoint startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        for (FavouritePlace favouritePlace : FavouritePlaceDao.FindAmountOrderByCounter(3)) {
            Suggestion suggestion = new Suggestion(favouritePlace.getAddress(), SuggestionAccuracy.MODERATE, FAVORITE_SUGGESTION);
            suggestions.add(suggestion);
        }

        return suggestions;
    }
}
