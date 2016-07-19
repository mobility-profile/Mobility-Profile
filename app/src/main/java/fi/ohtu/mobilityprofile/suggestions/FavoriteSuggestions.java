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
     * Returns the first saved favorite place.
     *
     * @return First favorite place
     */
    @Override
    public List<Suggestion> getSuggestions(String startLocation) {
        List<FavouritePlace> favouritePlaces = favouritePlaceDao.findAllOrderByCounter();

        List<Suggestion> suggestions = new ArrayList<>();
        if (!favouritePlaces.isEmpty()) {
            suggestions.add(new Suggestion(favouritePlaces.get(0).getAddress(), SuggestionAccuracy.MODERATE, FAVORITE_SUGGESTION));
        }

        return suggestions;
    }
}
