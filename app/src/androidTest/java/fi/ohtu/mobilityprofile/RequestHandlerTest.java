package fi.ohtu.mobilityprofile;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static fi.ohtu.mobilityprofile.RequestCode.*;

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
        requestHandler = new RequestHandler(null, new JourneyPlanner());
    }

    @Test
    public void testMostLikelyDestination() {
        Message message = Message.obtain(null, REQUEST_MOST_LIKELY_DESTINATION);
        message.replyTo = new Messenger(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                assertEquals(RESPOND_MOST_LIKELY_DESTINATION, msg.what);
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
                assertEquals(ERROR_UNKNOWN_CODE, msg.what);
            }
        });
        requestHandler.handleMessage(message);
    }
}
