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
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.Visit;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.sources.VisitSuggestions;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class VisitSuggestionsTest {
    private VisitSuggestions visitSuggestions;
    private List<Suggestion> suggestions;

    private Place kumpula;
    private Place toolo;
    private Place lauttasaari;
    private Place pitajanmaki;

    @Before
    public void setUp() {
        this.visitSuggestions = new VisitSuggestions();
        this.suggestions = new ArrayList<>();
        Robolectric.setupActivity(MainActivityStub.class);

        kumpula = new Place("Kumpula", "Kumpula", new Coordinate(new Float(1), new Float(2)));
        toolo = new Place("Töölö", "Töölö", new Coordinate(new Float(3), new Float(4)));
        lauttasaari = new Place("Lauttasaari", "Lauttasaari", new Coordinate(new Float(5), new Float(6)));
        pitajanmaki = new Place("Pitäjänmäki", "Pitäjänmäki", new Coordinate(new Float(7), new Float(8)));

        PlaceDao.insertPlace(kumpula);
        PlaceDao.insertPlace(toolo);
        PlaceDao.insertPlace(lauttasaari);
        PlaceDao.insertPlace(pitajanmaki);
    }

    @Test
    public void testGetSuggestionsWhenVisitsSizeIsMoreThanFour() {
        System.out.println("testGetSuggestionsWhenVisitsSizeIsMoreThanFoure");
        createListOfVisits();
        suggestions = visitSuggestions.getSuggestions(new GpsPoint(0, kumpula.getCoordinate().getLatitude(), kumpula.getCoordinate().getLongitude()));
        assertEquals(1, suggestions.size());
        assertEquals("Töölö", suggestions.get(0).getDestination());
    }

    @Test
    public void testGetSuggestionsWhenVisitSizeIsFour() {
        System.out.println("testGetSuggestionsWhenVisitSizeIsFour");
        createShortListOfVisits();
        suggestions = visitSuggestions.getSuggestions(new GpsPoint(0, kumpula.getCoordinate().getLatitude(), kumpula.getCoordinate().getLongitude()));
        assertEquals(0, suggestions.size());
    }
/*
    @Test
    public void testGetSuggestionsWhenStartLocationIsNotSignificantPlace() {

    }
*/
    @Test
    public void testGetTwoSuggestions() {
        System.out.println("testGetTwoSuggestions");
        createLongListOfVisits();
        suggestions = visitSuggestions.getSuggestions(new GpsPoint(0, kumpula.getCoordinate().getLatitude(), kumpula.getCoordinate().getLongitude()));
        assertEquals(2, suggestions.size());
        ArrayList<String> destinations = new ArrayList<>();
        destinations.add(suggestions.get(0).getDestination());
        destinations.add(suggestions.get(1).getDestination());

        assertTrue(destinations.contains("Töölö"));
        assertTrue(destinations.contains("Lauttasaari"));
    }

    @Test
    public void testGetNoSuggestions() {
        System.out.println("testGetNoSuggestions");
        createListOfVisits();
        VisitDao.insert(new Visit(6000, 6001, lauttasaari));
        suggestions = visitSuggestions.getSuggestions(new GpsPoint(0, lauttasaari.getCoordinate().getLatitude(), lauttasaari.getCoordinate().getLongitude()));
        assertEquals(0, suggestions.size());
    }

    private void createListOfVisits() {

        VisitDao.insert(new Visit(100, 101, kumpula));

        VisitDao.insert(new Visit(200, 201, toolo));
        VisitDao.insert(new Visit(300, 301, lauttasaari));
        VisitDao.insert(new Visit(400, 401, pitajanmaki));
        VisitDao.insert(new Visit(500, 501, kumpula));

        VisitDao.insert(new Visit(600, 601, toolo));
        VisitDao.insert(new Visit(700, 701, kumpula));
        VisitDao.insert(new Visit(800, 801, toolo));
        VisitDao.insert(new Visit(900, 901, lauttasaari));

        VisitDao.insert(new Visit(1000, 1001, toolo));
        VisitDao.insert(new Visit(2000, 2001, kumpula));
        VisitDao.insert(new Visit(3000, 3001, lauttasaari));
        VisitDao.insert(new Visit(4000, 4001, pitajanmaki));

        VisitDao.insert(new Visit(5000, 5001, kumpula));
    }

    private void createShortListOfVisits() {

        PlaceDao.insertPlace(kumpula);
        PlaceDao.insertPlace(toolo);
        PlaceDao.insertPlace(lauttasaari);
        PlaceDao.insertPlace(pitajanmaki);

        VisitDao.insert(new Visit(100, 101, kumpula));

        VisitDao.insert(new Visit(200, 201, toolo));
        VisitDao.insert(new Visit(300, 301, lauttasaari));
        VisitDao.insert(new Visit(400, 401, pitajanmaki));
    }

    public void createLongListOfVisits() {

        VisitDao.insert(new Visit(100, 101, kumpula));

        VisitDao.insert(new Visit(200, 201, toolo));
        VisitDao.insert(new Visit(300, 301, lauttasaari));
        VisitDao.insert(new Visit(400, 401, pitajanmaki));
        VisitDao.insert(new Visit(500, 501, kumpula));
        VisitDao.insert(new Visit(501, 502, toolo));

        VisitDao.insert(new Visit(510, 511, lauttasaari));
        VisitDao.insert(new Visit(512, 513, pitajanmaki));
        VisitDao.insert(new Visit(513, 514, kumpula));
        VisitDao.insert(new Visit(514, 515, lauttasaari));

        VisitDao.insert(new Visit(600, 601, toolo));
        VisitDao.insert(new Visit(700, 701, kumpula));
        VisitDao.insert(new Visit(800, 801, toolo));
        VisitDao.insert(new Visit(900, 901, lauttasaari));

        VisitDao.insert(new Visit(1000, 1001, toolo));
        VisitDao.insert(new Visit(2000, 2001, kumpula));
        VisitDao.insert(new Visit(3000, 3001, lauttasaari));
        VisitDao.insert(new Visit(4000, 4001, pitajanmaki));

        VisitDao.insert(new Visit(5000, 5001, kumpula));

    }


}
