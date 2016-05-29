package fi.ohtu.mobilityprofile;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RequestHandlerTest {
    RequestHandler requestHandler;
    JourneyPlanner journeyPlanner;

    @Before
    public void setUp() {
        journeyPlanner = mock(JourneyPlanner.class);
        requestHandler = new RequestHandler(null, journeyPlanner);
    }

    @Test
    public void testMostLikelyDestination() {
        Message message = Message.obtain(null, RequestHandler.REQUEST_MOST_LIKELY_DESTINATION);
        message.replyTo = new Messenger(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                assertEquals(RequestHandler.RESPOND_MOST_LIKELY_DESTINATION, msg.what);
            }
        });
        requestHandler.handleMessage(message);

        verify(journeyPlanner).getMostLikelyDestination(anyString());
    }

    @Test
    public void testUnknownRequest() {
        Message message = Message.obtain(null, 234234);
        message.replyTo = new Messenger(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                assertEquals(RequestHandler.ERROR_CODE_NOT_FOUND, msg.what);
            }
        });
        requestHandler.handleMessage(message);
    }
}
