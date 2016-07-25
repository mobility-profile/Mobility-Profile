package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

/**
 * This class creates suggestions based on the user's visits to places he has visited frequently in the past.
 */
public class SignificantPlaceSuggestions implements SuggestionSource {
    private SignificantPlaceDao significantPlaceDao;

    public SignificantPlaceSuggestions(SignificantPlaceDao dao) {
        this.significantPlaceDao = dao;
    }

    /**
     * Returns suggestions based on previous journeys between frequently visited places.
     *
     * @param startLocation Starting location
     * @return List of probable destinations
     */
    @Override
    public List<Suggestion> getSuggestions(String startLocation) {
        List<Suggestion> suggestions = new ArrayList<>();

        SignificantPlace significantPlace = significantPlaceDao.getSignificantPlaceIfCurrentLocationIsOne(startLocation, 100);


        return suggestions;
    }
}
