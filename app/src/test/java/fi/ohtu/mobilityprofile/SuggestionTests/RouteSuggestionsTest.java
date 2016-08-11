package fi.ohtu.mobilityprofile.SuggestionTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.sources.RouteSuggestions;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class RouteSuggestionsTest {
    private RouteSuggestions routeSuggestions;
    private final int HOUR = 3600 * 1000;
    private Coordinate lauttaGps;
    private Coordinate ruohoGps;
    private Coordinate kamppiGps;
    private Coordinate pitajanGps;
    private Coordinate sornGps;

    @Before
    public void setUp() {
        this.routeSuggestions = new RouteSuggestions();
        Robolectric.setupActivity(MainActivityStub.class);
        lauttaGps = new Coordinate(new Float(10), new Float(50));
        ruohoGps = new Coordinate(new Float(20), new Float(40));
        kamppiGps = new Coordinate(new Float(30), new Float(30));
        pitajanGps = new Coordinate(new Float(40), new Float(20));
        sornGps = new Coordinate(new Float(50), new Float(10));
    }

    @Test
    public void testGetCorrectSuggestion() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Lauttasaari", "Ruoholahti", lauttaGps, ruohoGps));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + 3 * HOUR, "Lauttasaari",  "Kamppi", lauttaGps, kamppiGps));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Pitäjänmäki",  "Sörnäinen", pitajanGps, sornGps));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions(new StartLocation(System.currentTimeMillis(), 0, lauttaGps.getLatitude(), lauttaGps.getLongitude()));
        assertEquals(1, suggestions.size());
        assertEquals("Ruoholahti", suggestions.get(0).getDestination());
    }

    @Test
    public void testGetZeroSuggestions() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Lauttasaari", "Ruoholahti", lauttaGps, ruohoGps));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Lauttasaari",  "Kamppi", lauttaGps, kamppiGps));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Pitäjänmäki",  "Sörnäinen", pitajanGps, sornGps));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + 3 * HOUR, "Ruoholahti", "Lauttasaari", ruohoGps, lauttaGps));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions(new StartLocation(0, 0, ruohoGps.getLatitude(), ruohoGps.getLongitude()));
        assertEquals(0, suggestions.size());
    }

    @Test
    public void testGetMultipleSuggestions() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() + HOUR, "Lauttasaari", "Ruoholahti", lauttaGps, ruohoGps));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis() - HOUR, "Lauttasaari",  "Kamppi", lauttaGps, kamppiGps));
        RouteSearchDao.insertRouteSearch(new RouteSearch(System.currentTimeMillis(), "Pitäjänmäki",  "Sörnäinen", pitajanGps, sornGps));

        List<Suggestion> suggestions = routeSuggestions.getSuggestions(new StartLocation(0, 0, lauttaGps.getLatitude(), lauttaGps.getLongitude()));
        assertEquals(2, suggestions.size());

        List<String> destinations = new ArrayList<>();
        destinations.add(suggestions.get(0).getDestination());
        destinations.add(suggestions.get(1).getDestination());
        assertTrue(destinations.contains("Ruoholahti"));
        assertTrue(destinations.contains("Kamppi"));
    }
}
