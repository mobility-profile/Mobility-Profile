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

import static org.junit.Assert.*;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.sources.RouteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class RouteSuggestionsTest {
    private RouteSuggestions routeSuggestions;
    private final int HOUR = 3600 * 1000;
    private Place kumpula;
    private Place sornainen;
    private Place lauttasaari;
    private Place hakaniemi;

    @Before
    public void setUp() {
        this.routeSuggestions = new RouteSuggestions();
        Robolectric.setupActivity(MainActivityStub.class);
        kumpula = new Place("Kumpula", new Address(Locale.getDefault()));
        sornainen = new Place("Sörnäinen", new Address(Locale.getDefault()));
        lauttasaari = new Place("Lauttasaari", new Address(Locale.getDefault()));
        hakaniemi = new Place("Hakaniemi", new Address(Locale.getDefault()));
        kumpula.setCoordinate(new Coordinate(new Float(60.209108), new Float(24.964735)));
        sornainen.setCoordinate(new Coordinate(new Float(60.186422), new Float(24.968971)));
        lauttasaari.setCoordinate(new Coordinate(new Float(60.157330), new Float(24.877253)));
        hakaniemi.setCoordinate(new Coordinate(new Float(60.17885), new Float(24.95006)));
    }

    @Test
    public void testGetCorrectSuggestion() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), 0, lauttasaari, sornainen));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + 3 * HOUR, 0, lauttasaari,  kumpula));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), 0, hakaniemi, sornainen));
        List<Suggestion> suggestions = routeSuggestions.getSuggestions(new StartLocation(System.currentTimeMillis(), 0, lauttasaari.getCoordinate().getLatitude(), lauttasaari.getCoordinate().getLongitude()));
        assertEquals(1, suggestions.size());
        assertEquals("Sörnäinen", suggestions.get(0).getDestination().getName());
    }

    @Test
    public void testGetZeroSuggestions() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), 0, lauttasaari, sornainen));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), 0, lauttasaari, kumpula));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), 0, hakaniemi, sornainen));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + 3 * HOUR, 0, sornainen, lauttasaari));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions(new StartLocation(System.currentTimeMillis(), 0, sornainen.getCoordinate().getLatitude(), sornainen.getCoordinate().getLongitude()));
        assertEquals(0, suggestions.size());
    }

    @Test
    public void testGetMultipleSuggestions() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + HOUR, 0 , lauttasaari, sornainen));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() - HOUR, 0, lauttasaari, kumpula));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), 0, hakaniemi, sornainen));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions(new StartLocation(System.currentTimeMillis(), 0, lauttasaari.getCoordinate().getLatitude(), lauttasaari.getCoordinate().getLongitude()));
        assertEquals(2, suggestions.size());

        List<String> destinations = new ArrayList<>();
        destinations.add(suggestions.get(0).getDestination().getName());
        destinations.add(suggestions.get(1).getDestination().getName());
        assertTrue(destinations.contains("Sörnäinen"));
        assertTrue(destinations.contains("Kumpula"));
    }
}
