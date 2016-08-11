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
import fi.ohtu.mobilityprofile.data.GpsPointDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;

import fi.ohtu.mobilityprofile.data.TransportModeDao;
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
    private GpsPointDao gpsPointDao;
    private RouteSearchDao routeSearchDao;
    private FavouritePlaceDao favouritePlaceDao;
    private TransportModeDao transportDao;

    @Before
    public void setUp() {
        this.context = Robolectric.setupActivity(MainActivityStub.class);;
        this.mobilityProfile = mock(DestinationLogic.class);
        this.calendarTagDao = mock(CalendarTagDao.class);
        this.routeSearchDao = new RouteSearchDao();
        this.gpsPointDao = new GpsPointDao();
        this.favouritePlaceDao = mock(FavouritePlaceDao.class);
        this.transportDao = mock(TransportModeDao.class);

        this.requestHandler = new RequestHandler(context, mobilityProfile);
    }

    @Test
    public void insertsASearchedRouteToDatabase() {
        Bundle bundle = new Bundle();
        bundle.putString(102+"", "Kumpula|Naantali");

        Message msg = new Message();
        msg.what = 102;
        msg.setData(bundle);

        requestHandler.handleMessage(msg);
        assertTrue(RouteSearchDao.getLatestRouteSearch().getDestination().equals("Naantali"));
    }
}
