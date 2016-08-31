package fi.ohtu.mobilityprofile.SuggestionTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.sources.InterCitySuggestions;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class InterCitySuggestionsTest {

    private InterCitySuggestions interCitySuggestions;
    private Place helsinki;
    private Place vaasa;
    private Place oulu;
    private Place jyvaskyla;
    private Place lahti;
    private Place tampere;
    private Place turku;

    @Before
    public void setUp() {
        this.interCitySuggestions = new InterCitySuggestions(Robolectric.setupActivity(MainActivityStub.class));
        helsinki = new Place();
        vaasa = new Place();
        oulu = new Place();
        jyvaskyla = new Place();
        lahti = new Place();
        tampere = new Place();
        turku = new Place();
        createInterCitySearches();
    }

    @Test
    public void getSuggestionsSuggestsOnlyDestinationsWithTheSameStartLocation() {
        List<Suggestion> results = interCitySuggestions.getSuggestions(new StartLocation(767565, 50, new Float(60.209108), new Float(24.964735)));
        assertEquals(2, results.size());
    }

    @Test
    public void getMaxFiveSuggestions() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(0, DestinationLogic.MODE_INTERCITY, helsinki, vaasa));
        RouteSearchDao.insertRouteSearch(new RouteSearch(0, DestinationLogic.MODE_INTERCITY, helsinki, oulu));
        RouteSearchDao.insertRouteSearch(new RouteSearch(0, DestinationLogic.MODE_INTERCITY, helsinki, jyvaskyla));
        RouteSearchDao.insertRouteSearch(new RouteSearch(0, DestinationLogic.MODE_INTERCITY, helsinki, lahti));
        List<Suggestion> results = interCitySuggestions.getSuggestions(new StartLocation(767565, 50, new Float(60.209108), new Float(24.964735)));
        assertEquals(5, results.size());
    }

    private void createInterCitySearches() {
        RouteSearchDao.insertRouteSearch(new RouteSearch(122324, DestinationLogic.MODE_INTERCITY, helsinki, tampere));
        RouteSearchDao.insertRouteSearch(new RouteSearch(232411, DestinationLogic.MODE_INTERCITY, tampere, turku));
        RouteSearchDao.insertRouteSearch(new RouteSearch(532434, DestinationLogic.MODE_INTERCITY, helsinki, tampere));
        RouteSearchDao.insertRouteSearch(new RouteSearch(772411, DestinationLogic.MODE_INTERCITY, helsinki, turku));
        RouteSearchDao.insertRouteSearch(new RouteSearch(885454, DestinationLogic.MODE_INTERCITY, turku, tampere));
        RouteSearchDao.insertRouteSearch(new RouteSearch(987541, DestinationLogic.MODE_INTERCITY, tampere, helsinki));
    }
}

