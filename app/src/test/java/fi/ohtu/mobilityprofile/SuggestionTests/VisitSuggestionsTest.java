package fi.ohtu.mobilityprofile.SuggestionTests;

import org.junit.Before;
import org.junit.Test;

import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;
import fi.ohtu.mobilityprofile.suggestions.locationHistory.VisitSuggestions;

public class VisitSuggestionsTest {
    private VisitSuggestions visitSuggestions;
    private SignificantPlaceDao significantPlaceDao;
    private VisitDao visitDao;

    @Before
    public void setUp() {
        this.significantPlaceDao = new SignificantPlaceDao();
        this.visitDao = new VisitDao();
        this.visitSuggestions = new VisitSuggestions(significantPlaceDao, visitDao);

    }

    @Test
    public void testGetSuggestionsWhenVisitsSizeIsMoreThanFour() {

    }

    @Test
    public void testGetSuggestionsWhenVisitSizeIsFour() {

    }

    @Test
    public void testGetSuggestionsWhenStartLocationIsNotSignificantPlace() {

    }


}
