package fi.ohtu.mobilityprofile.SuggestionTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.SignificantPlace;
import fi.ohtu.mobilityprofile.domain.Visit;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.sources.VisitSuggestions;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class VisitSuggestionsTest {
    private VisitSuggestions visitSuggestions;
    private SignificantPlaceDao significantPlaceDao;
    private VisitDao visitDao;
    private List<Suggestion> suggestions;

    private SignificantPlace kumpula;
    private SignificantPlace toolo;
    private SignificantPlace lauttasaari;
    private SignificantPlace pitajanmaki;

    @Before
    public void setUp() {
        this.significantPlaceDao = new SignificantPlaceDao();
        this.visitDao = new VisitDao();
        this.visitSuggestions = new VisitSuggestions(significantPlaceDao, visitDao);
        this.suggestions = new ArrayList<>();
        Robolectric.setupActivity(MainActivityStub.class);

        kumpula = new SignificantPlace("Kumpula", "Kumpula", new Coordinate(new Float(1), new Float(2)));
        toolo = new SignificantPlace("Töölö", "Töölö", new Coordinate(new Float(1), new Float(2)));
        lauttasaari = new SignificantPlace("Lauttasaari", "Lauttasaari", new Coordinate(new Float(1), new Float(2)));
        pitajanmaki = new SignificantPlace("Pitäjänmäki", "Pitäjänmäki", new Coordinate(new Float(1), new Float(2)));

        significantPlaceDao.insertSignificantPlace(kumpula);
        significantPlaceDao.insertSignificantPlace(toolo);
        significantPlaceDao.insertSignificantPlace(lauttasaari);
        significantPlaceDao.insertSignificantPlace(pitajanmaki);
    }

    @Test
    public void testGetSuggestionsWhenVisitsSizeIsMoreThanFour() {
        createListOfVisits();
        suggestions = visitSuggestions.getSuggestions("Kumpula");
        assertEquals(1, suggestions.size());
        assertEquals("Töölö", suggestions.get(0).getDestination());
    }

    @Test
    public void testGetSuggestionsWhenVisitSizeIsFour() {
        createShortListOfVisits();
        suggestions = visitSuggestions.getSuggestions("Kumpula");
        assertEquals(0, suggestions.size());
    }
/*
    @Test
    public void testGetSuggestionsWhenStartLocationIsNotSignificantPlace() {

    }
*/
    @Test
    public void testGetTwoSuggestions() {
        createLongListOfVisits();
        suggestions = visitSuggestions.getSuggestions("Kumpula");
        assertEquals(2, suggestions.size());
        ArrayList<String> destinations = new ArrayList<>();
        destinations.add(suggestions.get(0).getDestination());
        destinations.add(suggestions.get(1).getDestination());

        assertTrue(destinations.contains("Töölö"));
        assertTrue(destinations.contains("Lauttasaari"));
    }

    @Test
    public void testGetNoSuggestions() {
        createListOfVisits();
        visitDao.insertVisit(new Visit(6000, lauttasaari));
        suggestions = visitSuggestions.getSuggestions("Lauttasaari");
        assertEquals(0, suggestions.size());
    }

    private void createListOfVisits() {

        visitDao.insertVisit(new Visit(100, kumpula));

        visitDao.insertVisit(new Visit(200, toolo));
        visitDao.insertVisit(new Visit(300, lauttasaari));
        visitDao.insertVisit(new Visit(400, pitajanmaki));
        visitDao.insertVisit(new Visit(500, kumpula));

        visitDao.insertVisit(new Visit(600, toolo));
        visitDao.insertVisit(new Visit(700, kumpula));
        visitDao.insertVisit(new Visit(800, toolo));
        visitDao.insertVisit(new Visit(900, lauttasaari));

        visitDao.insertVisit(new Visit(1000, toolo));
        visitDao.insertVisit(new Visit(2000, kumpula));
        visitDao.insertVisit(new Visit(3000, lauttasaari));
        visitDao.insertVisit(new Visit(4000, pitajanmaki));

        visitDao.insertVisit(new Visit(5000, kumpula));
    }

    private void createShortListOfVisits() {

        significantPlaceDao.insertSignificantPlace(kumpula);
        significantPlaceDao.insertSignificantPlace(toolo);
        significantPlaceDao.insertSignificantPlace(lauttasaari);
        significantPlaceDao.insertSignificantPlace(pitajanmaki);

        visitDao.insertVisit(new Visit(100, kumpula));

        visitDao.insertVisit(new Visit(200, toolo));
        visitDao.insertVisit(new Visit(300, lauttasaari));
        visitDao.insertVisit(new Visit(400, pitajanmaki));
    }

    public void createLongListOfVisits() {

        visitDao.insertVisit(new Visit(100, kumpula));

        visitDao.insertVisit(new Visit(200, toolo));
        visitDao.insertVisit(new Visit(300, lauttasaari));
        visitDao.insertVisit(new Visit(400, pitajanmaki));
        visitDao.insertVisit(new Visit(500, kumpula));
        visitDao.insertVisit(new Visit(501, toolo));

        visitDao.insertVisit(new Visit(510, lauttasaari));
        visitDao.insertVisit(new Visit(512, pitajanmaki));
        visitDao.insertVisit(new Visit(513, kumpula));
        visitDao.insertVisit(new Visit(514, lauttasaari));

        visitDao.insertVisit(new Visit(600, toolo));
        visitDao.insertVisit(new Visit(700, kumpula));
        visitDao.insertVisit(new Visit(800, toolo));
        visitDao.insertVisit(new Visit(900, lauttasaari));

        visitDao.insertVisit(new Visit(1000, toolo));
        visitDao.insertVisit(new Visit(2000, kumpula));
        visitDao.insertVisit(new Visit(3000, lauttasaari));
        visitDao.insertVisit(new Visit(4000, pitajanmaki));

        visitDao.insertVisit(new Visit(5000, kumpula));

    }


}
