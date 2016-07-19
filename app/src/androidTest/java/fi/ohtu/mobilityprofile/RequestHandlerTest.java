/*
package fi.ohtu.mobilityprofile;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.UserLocationDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.remoteconnection.RequestCode;
import fi.ohtu.mobilityprofile.remoteconnection.RequestHandler;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class RequestHandlerTest {
    private RequestHandler requestHandler;

    public RequestHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
    }

    @Before
    public void setUp() {
        CalendarTagDao calendarTagDao = new CalendarTagDao();
        requestHandler = new RequestHandler(null, new DestinationLogic(this, calendarTagDao,
                new VisitDao(new UserLocationDao()), new RouteSearchDao(), new FavouritePlaceDao()), calendarTagDao);
    }

    @Test
    public void testMostLikelyDestination() {
        Message message = Message.obtain(null, RequestCode.REQUEST_MOST_LIKELY_DESTINATION);
        message.replyTo = new Messenger(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                assertEquals(RequestCode.RESPOND_MOST_LIKELY_DESTINATION, msg.what);
            }
        });
        requestHandler.handleMessage(message);
    }

    @Test
    public void testUnknownRequest() {
        Message message = Message.obtain(null, 234234);
        message.replyTo = new Messenger(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                assertEquals(RequestCode.ERROR_UNKNOWN_CODE, msg.what);
            }
        });
        requestHandler.handleMessage(message);
    }
}
*/