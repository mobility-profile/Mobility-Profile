package fi.ohtu.mobilityprofile;


import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.RouteSearch;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.UserLocationDao;
import fi.ohtu.mobilityprofile.data.VisitDao;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk = 21)
public class RequestHandlerTest {

    private RequestHandler requestHandler;
    private Context context;
    private MobilityProfile mobilityProfile;
    private CalendarTagDao calendarTagDao;
    private VisitDao visitDao;
    private RouteSearchDao routeSearchDao;

    @Before
    public void setUp() {
        this.context = null;
        this.mobilityProfile = mock(MobilityProfile.class);
        this.calendarTagDao = mock(CalendarTagDao.class);
        this.routeSearchDao = new RouteSearchDao();
        this.visitDao = new VisitDao(new UserLocationDao());

        this.requestHandler = new RequestHandler(context, mobilityProfile, calendarTagDao, visitDao, routeSearchDao);

        when(mobilityProfile.isCalendarDestination()).thenReturn(false);

        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void insertsASearchedRoutetoDatabase() {
        Bundle bundle = new Bundle();
        bundle.putString(102+"", "Naantali");

        Message msg = new Message();
        msg.what = 102;
        msg.setData(bundle);

        requestHandler.handleMessage(msg);
        assertTrue(routeSearchDao.getLatestRouteSearch().getDestination().equals("Naantali"));
    }
}
