package fi.ohtu.mobilityprofile.suggestions.sources;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.StartLocation;
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
     * Returns the favorite places.
     *
     * @param startLocation Location where the user starts the journey
     * @return The favorite places
     */
    @Override
    public List<Suggestion> getSuggestions(StartLocation startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        for (Place favouritePlace : PlaceDao.getFavouritePlaces()) {
            Suggestion suggestion = new Suggestion(favouritePlace.getAddress(), SuggestionAccuracy.MODERATE, FAVORITE_SUGGESTION, favouritePlace.getCoordinate());
            suggestions.add(suggestion);
        }

        return suggestions;
    }
}
