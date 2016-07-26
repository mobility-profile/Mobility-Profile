package fi.ohtu.mobilityprofile;


import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.SignificantPlaceDao;
import fi.ohtu.mobilityprofile.remoteconnection.RequestHandler;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;

import static org.mockito.Mockito.mock;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class RequestHandlerTest {

    private RequestHandler requestHandler;
    private Context context;
    private DestinationLogic mobilityProfile;
    private CalendarTagDao calendarTagDao;
    private PlaceDao placeDao;
    private RouteSearchDao routeSearchDao;
    private FavouritePlaceDao favouritePlaceDao;

    @Before
    public void setUp() {
        this.context = null;
        this.mobilityProfile = mock(DestinationLogic.class);
        this.calendarTagDao = mock(CalendarTagDao.class);
        this.routeSearchDao = new RouteSearchDao();
        this.placeDao = new PlaceDao(new SignificantPlaceDao());
        this.favouritePlaceDao = mock(FavouritePlaceDao.class);

        this.requestHandler = new RequestHandler(mobilityProfile, calendarTagDao, placeDao, routeSearchDao, favouritePlaceDao);

        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void insertsASearchedRouteToDatabase() {
        Bundle bundle = new Bundle();
        bundle.putString(102+"", "Kumpula Naantali");

        Message msg = new Message();
        msg.what = 102;
        msg.setData(bundle);

        requestHandler.handleMessage(msg);
        assertTrue(routeSearchDao.getLatestRouteSearch().getDestination().equals("Naantali"));
    }
}
