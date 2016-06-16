package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.*;

import fi.ohtu.mobilityprofile.data.RouteSearch;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk = 21)
public class RouteSearchDaoTest {
    private RouteSearchDao routeSearchDao;

    @Before
    public void setUp() {
        this.routeSearchDao = new RouteSearchDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void insertRouteSearchTest() {
        routeSearchDao.insertRouteSearch(new RouteSearch(1, "Ruoholahti"));
        assertEquals(routeSearchDao.getLatestRouteSearch().getLocation(), "Ruoholahti");
    }

    @Test
    public void getLatestRouteSearchTest() {
        routeSearchDao.insertRouteSearch(new RouteSearch(2, "Kamppi"));
        assertEquals("Kamppi", routeSearchDao.getLatestRouteSearch().getLocation());

        routeSearchDao.insertRouteSearch(new RouteSearch(3, "Rautatientori"));
        assertEquals("Rautatientori", routeSearchDao.getLatestRouteSearch().getLocation());

        routeSearchDao.insertRouteSearch(new RouteSearch(4, "Helsingin yliopisto"));
        routeSearchDao.insertRouteSearch(new RouteSearch(5, "Hakaniemi"));

        assertEquals("Hakaniemi", routeSearchDao.getLatestRouteSearch().getLocation());
    }

    @Test
    public void getRouteSearchesByLocationTest() {
        routeSearchDao.insertRouteSearch(new RouteSearch(1, "Sörnäinen"));
        routeSearchDao.insertRouteSearch(new RouteSearch(2, "Sörnäinen"));
        routeSearchDao.insertRouteSearch(new RouteSearch(3, "Kalasatama"));
        routeSearchDao.insertRouteSearch(new RouteSearch(4, "Sörnäinen"));

        List<RouteSearch> searches = routeSearchDao.getRouteSearchesByLocation("Sörnäinen");

        assertEquals(4, searches.get(0).getTimestamp());
        assertEquals(2, searches.get(1).getTimestamp());
        assertEquals(1, searches.get(2).getTimestamp());
        assertEquals(3, searches.size());

    }

    @Test
    public void getLatestFindsNothing() {
        assertEquals(null, routeSearchDao.getLatestRouteSearch());
    }

    @Test
    public void getByLocationFindsNothing() {
        assertTrue(routeSearchDao.getRouteSearchesByLocation("Aurinko").isEmpty());
    }
}
