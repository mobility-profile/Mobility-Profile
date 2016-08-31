package fi.ohtu.mobilityprofile.remoteconnection;

public class RequestCode {
    public static final int REQUEST_SUGGESTIONS = 100;
    public static final int SEND_SEARCHED_ROUTE = 110;
    public static final int REQUEST_TRANSPORT_PREFERENCES = 120;

    public static final int RESPOND_MOST_LIKELY_DESTINATION = 200;
    public static final int RESPOND_TRANSPORT_PREFERENCES = 220;
    public static final int RESPOND_FAVOURITE_PLACES = 230;

    public static final int MODE_INTRACITY = 300;
    public static final int MODE_INTERCITY = 301;

    public static final int ERROR_UNKNOWN_REQUEST = 400;
    public static final int ERROR_UNKNOWN_MODE = 410;
}
