package fi.ohtu.mobilityprofile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;
import java.util.Date;
import java.util.ArrayList;
import fi.ohtu.mobilityprofile.data.CalendarTag;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearch;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.data.Visit;
import fi.ohtu.mobilityprofile.data.VisitDao;

import static fi.ohtu.mobilityprofile.RequestCode.*;

/**
 * Used for processing incoming requests from other apps.
 */
public class RequestHandler extends Handler {
    private Context context;
    private MobilityProfile mobilityProfile;
    private CalendarTagDao calendarTagDao;
    private VisitDao visitDao;
    private RouteSearchDao routeSearchDao;
    private FavouritePlaceDao favouritePlaceDao;

    /**
     * Creates the RequestHandler.
     *
     * @param context Context used for toast messages
     * @param mobilityProfile Journey planner that provides the logic for our app
     * @param calendarTagDao DAO for calendar tags
     * @param visitDao DAO for visits
     * @param routeSearchDao DAO for routeSearch
     * @param favouritePlaceDao for favourite places
     */
    public RequestHandler(Context context, MobilityProfile mobilityProfile,
                          CalendarTagDao calendarTagDao, VisitDao visitDao, RouteSearchDao routeSearchDao, FavouritePlaceDao favouritePlaceDao) {
        this.context = context;
        this.mobilityProfile = mobilityProfile;
        this.calendarTagDao = calendarTagDao;
        this.visitDao = visitDao;
        this.routeSearchDao = routeSearchDao;
        this.favouritePlaceDao = favouritePlaceDao;
    }

    @Override
    public void handleMessage(Message msg) {
        // For testing
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), "Remote Service invoked (" + msg.what + ")", Toast.LENGTH_SHORT).show();
        }

        Message message;
        switch (msg.what) {
            case REQUEST_MOST_LIKELY_DESTINATION:
                message = processDestinationRequest();
                break;
            case SEND_USED_DESTINATION:
                processUsedRoute(msg);
                return;
            case RESPOND_FAVOURITE_PLACES:
                message = getFavouritePlaces();
                break;
            default:
                message = processErrorMessage(msg.what);
        }

        try {
            // Make the RPC invocation
            msg.replyTo.send(message);
        } catch (RemoteException rme) {
            if (context != null) {
                Toast.makeText(context, "Invocation failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *
     * @return
     */
    private Message processDestinationRequest() {
        return createMessage(RESPOND_MOST_LIKELY_DESTINATION, mobilityProfile.getMostLikelyDestination(getStartLocation()));
    }

    /**
     *
     * @param message
     */
    private void processUsedRoute(Message message) {
        Bundle bundle = message.getData();
        String destination = bundle.getString(SEND_USED_DESTINATION+"");
        if (mobilityProfile.isCalendarDestination()) {
            CalendarTag calendarTag = new CalendarTag(mobilityProfile.getLatestDestination(), destination);
            calendarTagDao.insertCalendarTag(calendarTag);
        } else {
            Date date = new Date();
            RouteSearch routeSearch = new RouteSearch(date.getTime(), getStartLocation(), destination);
            routeSearchDao.insertRouteSearch(routeSearch);
        }
    }

    /**
     *
     * @return
     */
    private String getStartLocation() {
        Visit lastKnownVisit = visitDao.getLatestVisit();
        if (lastKnownVisit == null) {
            // TODO something better
            return "None";
        } else {
            return lastKnownVisit.getOriginalLocation();
        }
    }

    /**
     *
     * @param code
     * @return
     */
    private Message processErrorMessage(int code) {
        return createMessage(ERROR_UNKNOWN_CODE, code+"");
    }

    /**
     *
     * @param code
     * @param info
     * @return
     */
    private Message createMessage(int code, String info)  {
        // Setup the reply message
        Bundle bundle = new Bundle();
        bundle.putString(code+"", info);
        Message message = Message.obtain(null, code);
        message.setData(bundle);

        return message;
    }
    
    /**
     * Create message that data is String array list.
     * @param code Message code
     * @param info Data of message, String array list
     * @return message
     */
    private Message createMessage(int code, ArrayList<String> info) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(code+"", info);
        Message message = Message.obtain(null, code);
        message.setData(bundle);

        return message;
    }
    
    /**
     * @return user's favourite places
     */
    private Message getFavouritePlaces() {
        return createMessage(RESPOND_FAVOURITE_PLACES, favouritePlaceDao.getNamesOfFavouritePlaces());
    }
}
