package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.*;

import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class RouteSearchDaoTest {
    private RouteSearchDao routeSearchDao;

    @Before
    public void setUp() {
        this.routeSearchDao = new RouteSearchDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void insertRouteSearchTest() {
        routeSearchDao.insertRouteSearch(new RouteSearch(1, "Lauttasaari", "Ruoholahti"));
        assertEquals(routeSearchDao.getLatestRouteSearch().getStartlocation(), "Lauttasaari");
        assertEquals(routeSearchDao.getLatestRouteSearch().getDestination(), "Ruoholahti");
    }

    @Test
    public void getLatestRouteSearchTest() {
        routeSearchDao.insertRouteSearch(new RouteSearch(2, "Lauttasaari",  "Kamppi"));
        assertEquals("Lauttasaari", routeSearchDao.getLatestRouteSearch().getStartlocation());
        assertEquals("Kamppi", routeSearchDao.getLatestRouteSearch().getDestination());

        routeSearchDao.insertRouteSearch(new RouteSearch(3, "Lauttasaari",  "Rautatientori"));
        assertEquals("Lauttasaari", routeSearchDao.getLatestRouteSearch().getStartlocation());
        assertEquals("Rautatientori", routeSearchDao.getLatestRouteSearch().getDestination());

        routeSearchDao.insertRouteSearch(new RouteSearch(4, "Lauttasaari",  "Helsingin yliopisto"));
        routeSearchDao.insertRouteSearch(new RouteSearch(5, "Lauttasaari",  "Hakaniemi"));

        assertEquals("Lauttasaari", routeSearchDao.getLatestRouteSearch().getStartlocation());
        assertEquals("Hakaniemi", routeSearchDao.getLatestRouteSearch().getDestination());
    }

    @Test
    public void getRouteSearchesByDestinationTest() {
        routeSearchDao.insertRouteSearch(new RouteSearch(1, "Lauttasaari",  "Sörnäinen"));
        routeSearchDao.insertRouteSearch(new RouteSearch(2, "Lauttasaari",  "Sörnäinen"));
        routeSearchDao.insertRouteSearch(new RouteSearch(3, "Lauttasaari",  "Kalasatama"));
        routeSearchDao.insertRouteSearch(new RouteSearch(4, "Lauttasaari",  "Sörnäinen"));

        List<RouteSearch> searches = routeSearchDao.getRouteSearchesByDestination("Sörnäinen");

        assertEquals(4, searches.get(0).getTimestamp());
        assertEquals(2, searches.get(1).getTimestamp());
        assertEquals(1, searches.get(2).getTimestamp());
        assertEquals(3, searches.size());

    }

    @Test
    public void getRouteSearchesByStartLocationTest() {
        routeSearchDao.insertRouteSearch(new RouteSearch(1, "Lauttasaari",  "Sörnäinen"));
        routeSearchDao.insertRouteSearch(new RouteSearch(2, "Lauttasaari",  "Sörnäinen"));
        routeSearchDao.insertRouteSearch(new RouteSearch(3, "Hakaniemi",  "Kalasatama"));
        routeSearchDao.insertRouteSearch(new RouteSearch(4, "Pitäjänmäki",  "Sörnäinen"));

        List<RouteSearch> searches = routeSearchDao.getRouteSearchesByStartlocation("Lauttasaari");

        assertEquals(2, searches.get(0).getTimestamp());
        assertEquals(1, searches.get(1).getTimestamp());
        assertEquals(2, searches.size());

    }

    @Test
    public void getRouteSearchesByStartlocationAndDestination() {
        routeSearchDao.insertRouteSearch(new RouteSearch(1, "Lauttasaari",  "Sörnäinen"));
        routeSearchDao.insertRouteSearch(new RouteSearch(2, "Lauttasaari",  "Herttoniemi"));
        routeSearchDao.insertRouteSearch(new RouteSearch(3, "Hakaniemi",  "Kalasatama"));
        routeSearchDao.insertRouteSearch(new RouteSearch(4, "Pitäjänmäki",  "Sörnäinen"));

        List<RouteSearch> searches = routeSearchDao.getRouteSearchesByStartlocationAndDestination("Lauttasaari","Herttoniemi");

        assertEquals(2, searches.get(0).getTimestamp());
        assertEquals(1, searches.size());
    }

    @Test
    public void getLatestFindsNothingReturnsNull() {
        assertEquals(null, routeSearchDao.getLatestRouteSearch());
    }

    @Test
    public void getByDestinationFindsNothingIsEmpty() {
        assertTrue(routeSearchDao.getRouteSearchesByDestination("Aurinko").isEmpty());
    }

    @Test
    public void getByStartlocationFindsNothingIsEmpty() {
        assertTrue(routeSearchDao.getRouteSearchesByStartlocation("Aurinko").isEmpty());
    }

    @Test
    public void getByStartlocationAndDestinationFindsNothingIsEmpty() {
        assertTrue(routeSearchDao.getRouteSearchesByStartlocationAndDestination("Aurinko", "Mars").isEmpty());
    }

    @Test
    public void testDeleteAll() {
        routeSearchDao.insertRouteSearch(new RouteSearch(1, "Lauttasaari",  "Sörnäinen"));
        routeSearchDao.insertRouteSearch(new RouteSearch(2, "Lauttasaari",  "Herttoniemi"));

        assertEquals(2, routeSearchDao.getRouteSearchesByStartlocation("Lauttasaari").size());

        RouteSearchDao.deleteAllData();

        assertEquals(0, routeSearchDao.getRouteSearchesByStartlocation("Lauttasaari").size());
    }
}
