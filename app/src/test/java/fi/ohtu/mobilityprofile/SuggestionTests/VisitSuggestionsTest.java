package fi.ohtu.mobilityprofile.SuggestionTests;

import android.location.Address;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.StartLocation;
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
    private Place sornainen;
    private Place lauttasaari;
    private Place hakaniemi;

    @Before
    public void setUp() {
        this.visitSuggestions = new VisitSuggestions();
        this.suggestions = new ArrayList<>();
        Robolectric.setupActivity(MainActivityStub.class);

        kumpula = new Place("Kumpula", new Address(Locale.getDefault()));
        sornainen = new Place("Sörnäinen", new Address(Locale.getDefault()));
        lauttasaari = new Place("Lauttasaari", new Address(Locale.getDefault()));
        hakaniemi = new Place("Hakaniemi", new Address(Locale.getDefault()));
        kumpula.setCoordinate(new Coordinate(new Float(60.209108), new Float(24.964735)));
        sornainen.setCoordinate(new Coordinate(new Float(60.186422), new Float(24.968971)));
        lauttasaari.setCoordinate(new Coordinate(new Float(60.157330), new Float(24.877253)));
        hakaniemi.setCoordinate(new Coordinate(new Float(60.17885), new Float(24.95006)));

        PlaceDao.insertPlace(kumpula);
        PlaceDao.insertPlace(sornainen);
        PlaceDao.insertPlace(lauttasaari);
        PlaceDao.insertPlace(hakaniemi);
    }

    @Test
    public void testGetSuggestionsWhenVisitsSizeIsMoreThanFour() {
        System.out.println("testGetSuggestionsWhenVisitsSizeIsMoreThanFoure");
        createListOfVisits();
        suggestions = visitSuggestions.getSuggestions(new StartLocation(0, 0, kumpula.getCoordinate().getLatitude(), kumpula.getCoordinate().getLongitude()));
        assertEquals(1, suggestions.size());
        assertEquals("Sörnäinen", suggestions.get(0).getDestination());
    }

    @Test
    public void testGetSuggestionsWhenVisitSizeIsFour() {
        System.out.println("testGetSuggestionsWhenVisitSizeIsFour");
        createShortListOfVisits();
        suggestions = visitSuggestions.getSuggestions(new StartLocation(0, 0, kumpula.getCoordinate().getLatitude(), kumpula.getCoordinate().getLongitude()));
        assertEquals(0, suggestions.size());
    }

    @Test
    public void testGetNoSuggestionsWhenStartLocationIsNotPlace() {
        createListOfVisits();
        suggestions = visitSuggestions.getSuggestions(new StartLocation(0, 0, new Float(80), new Float(90)));
        assertEquals(0, suggestions.size());
    }

    @Test
    public void testGetTwoSuggestions() {
        System.out.println("testGetTwoSuggestions");
        createLongListOfVisits();
        suggestions = visitSuggestions.getSuggestions(new StartLocation(0, 0, kumpula.getCoordinate().getLatitude(), kumpula.getCoordinate().getLongitude()));
        assertEquals(2, suggestions.size());
        ArrayList<String> destinations = new ArrayList<>();
        destinations.add(suggestions.get(0).getDestination().getName());
        destinations.add(suggestions.get(1).getDestination().getName());

        assertTrue(destinations.contains("Sörnäinen"));
        assertTrue(destinations.contains("Lauttasaari"));
    }

    @Test
    public void testGetNoSuggestions() {
        System.out.println("testGetNoSuggestions");
        createShortishListOfVisits();
        VisitDao.insert(new Visit(6000, 6001, lauttasaari));
        suggestions = visitSuggestions.getSuggestions(new StartLocation(0, 0, lauttasaari.getCoordinate().getLatitude(), lauttasaari.getCoordinate().getLongitude()));
        assertEquals(0, suggestions.size());
    }

    @Test
    public void testGetLowerAccuracySuggestions() {
        createListForLowerAccuracyVisits();

        suggestions = visitSuggestions.getSuggestions(new StartLocation(0, 0, lauttasaari.getCoordinate().getLatitude(), lauttasaari.getCoordinate().getLongitude()));
        assertEquals(1, suggestions.size());
        assertEquals("Sörnäinen", suggestions.get(0).getDestination());
    }

    private void createListOfVisits() {

        VisitDao.insert(new Visit(100, 101, kumpula));

        VisitDao.insert(new Visit(200, 201, sornainen));
        VisitDao.insert(new Visit(300, 301, lauttasaari));
        VisitDao.insert(new Visit(400, 401, hakaniemi));
        VisitDao.insert(new Visit(500, 501, kumpula));

        VisitDao.insert(new Visit(600, 601, sornainen));
        VisitDao.insert(new Visit(700, 701, kumpula));
        VisitDao.insert(new Visit(800, 801, sornainen));
        VisitDao.insert(new Visit(900, 901, lauttasaari));

        VisitDao.insert(new Visit(1000, 1001, sornainen));
        VisitDao.insert(new Visit(2000, 2001, kumpula));
        VisitDao.insert(new Visit(3000, 3001, lauttasaari));
        VisitDao.insert(new Visit(4000, 4001, hakaniemi));

        VisitDao.insert(new Visit(5000, 5001, kumpula));
    }

    private void createShortListOfVisits() {

        PlaceDao.insertPlace(kumpula);
        PlaceDao.insertPlace(sornainen);
        PlaceDao.insertPlace(lauttasaari);
        PlaceDao.insertPlace(hakaniemi);

        VisitDao.insert(new Visit(100, 101, kumpula));

        VisitDao.insert(new Visit(200, 201, sornainen));
        VisitDao.insert(new Visit(300, 301, lauttasaari));
        VisitDao.insert(new Visit(400, 401, hakaniemi));
    }

    private void createShortishListOfVisits() {
        PlaceDao.insertPlace(kumpula);
        PlaceDao.insertPlace(sornainen);
        PlaceDao.insertPlace(lauttasaari);
        PlaceDao.insertPlace(hakaniemi);

        VisitDao.insert(new Visit(100, 101, kumpula));

        VisitDao.insert(new Visit(200, 201, sornainen));
        VisitDao.insert(new Visit(400, 401, hakaniemi));

        VisitDao.insert(new Visit(600, 601, sornainen));
        VisitDao.insert(new Visit(700, 701, kumpula));
        VisitDao.insert(new Visit(800, 801, sornainen));
        VisitDao.insert(new Visit(900, 901, lauttasaari));

        VisitDao.insert(new Visit(1000, 1001, sornainen));
        VisitDao.insert(new Visit(2000, 2001, kumpula));
        VisitDao.insert(new Visit(3000, 3001, lauttasaari));
    }

    public void createLongListOfVisits() {

        VisitDao.insert(new Visit(100, 101, kumpula));

        VisitDao.insert(new Visit(200, 201, sornainen));
        VisitDao.insert(new Visit(300, 301, lauttasaari));
        VisitDao.insert(new Visit(400, 401, hakaniemi));
        VisitDao.insert(new Visit(500, 501, kumpula));
        VisitDao.insert(new Visit(501, 502, sornainen));

        VisitDao.insert(new Visit(510, 511, lauttasaari));
        VisitDao.insert(new Visit(512, 513, hakaniemi));
        VisitDao.insert(new Visit(513, 514, kumpula));
        VisitDao.insert(new Visit(514, 515, lauttasaari));

        VisitDao.insert(new Visit(600, 601, sornainen));
        VisitDao.insert(new Visit(700, 701, kumpula));
        VisitDao.insert(new Visit(800, 801, sornainen));
        VisitDao.insert(new Visit(900, 901, lauttasaari));

        VisitDao.insert(new Visit(1000, 1001, sornainen));
        VisitDao.insert(new Visit(2000, 2001, kumpula));
        VisitDao.insert(new Visit(3000, 3001, lauttasaari));
        VisitDao.insert(new Visit(4000, 4001, hakaniemi));

        VisitDao.insert(new Visit(5000, 5001, kumpula));
    }

    public void createListForLowerAccuracyVisits() {
        VisitDao.insert(new Visit(100, 101, kumpula));

        VisitDao.insert(new Visit(200, 201, sornainen));
        VisitDao.insert(new Visit(300, 301, lauttasaari));
        VisitDao.insert(new Visit(400, 401, hakaniemi));
        VisitDao.insert(new Visit(500, 501, kumpula));

        VisitDao.insert(new Visit(600, 601, sornainen));
        VisitDao.insert(new Visit(700, 701, kumpula));
        VisitDao.insert(new Visit(800, 801, sornainen));
        VisitDao.insert(new Visit(805, 810, kumpula));
        VisitDao.insert(new Visit(900, 901, lauttasaari));

        VisitDao.insert(new Visit(1000, 1001, sornainen));
        VisitDao.insert(new Visit(2000, 2001, kumpula));
        VisitDao.insert(new Visit(4000, 4001, hakaniemi));

        VisitDao.insert(new Visit(5000, 5001, kumpula));
        VisitDao.insert(new Visit(6000, 6001, lauttasaari));
    }


}
