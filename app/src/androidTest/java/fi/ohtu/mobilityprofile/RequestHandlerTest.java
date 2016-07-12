package fi.ohtu.mobilityprofile;

/*
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
        requestHandler = new RequestHandler(null, new MobilityProfile(calendarTagDao), calendarTagDao);
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
*/