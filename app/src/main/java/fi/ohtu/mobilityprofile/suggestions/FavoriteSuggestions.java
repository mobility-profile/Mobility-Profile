package fi.ohtu.mobilityprofile.suggestions;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.domain.FavouritePlace;

public class FavoriteSuggestions implements SuggestionSource {
    private FavouritePlaceDao favouritePlaceDao;

    public FavoriteSuggestions(FavouritePlaceDao favouritePlaceDao) {
        this.favouritePlaceDao = favouritePlaceDao;
    }

    /**
     * Returns three most used favorite places.
     *
     * @return Three favorite places
     */
    @Override
    public List<Suggestion> getSuggestions(String startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        for (FavouritePlace favouritePlace : favouritePlaceDao.FindAmountOrderByCounter(3)) {
            Suggestion suggestion = new Suggestion(favouritePlace.getAddress(), SuggestionAccuracy.MODERATE, FAVORITE_SUGGESTION);
            suggestions.add(suggestion);
        }

        return suggestions;
    }
}
