package fi.ohtu.mobilityprofile.remoteconnection;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.domain.CalendarTag;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.domain.FavouritePlace;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

import static fi.ohtu.mobilityprofile.remoteconnection.RequestCode.*;

/**
 * Used for processing incoming requests from other apps.
 */
public class RequestHandler extends Handler {
    private Context context;
    private DestinationLogic mobilityProfile;
    private CalendarTagDao calendarTagDao;
    private PlaceDao placeDao;
    private RouteSearchDao routeSearchDao;
    private FavouritePlaceDao favouritePlaceDao;

    /**
     * Creates the RequestHandler.
     *
     * @param mobilityProfile Journey planner that provides the logic for our app
     * @param calendarTagDao DAO for calendar tags
     * @param placeDao DAO for visits
     * @param routeSearchDao DAO for routeSearch
     * @param favouritePlaceDao DAO for favourite places
     */
    public RequestHandler(DestinationLogic mobilityProfile, CalendarTagDao calendarTagDao,
                          PlaceDao placeDao, RouteSearchDao routeSearchDao, FavouritePlaceDao favouritePlaceDao) {
        this.mobilityProfile = mobilityProfile;
        this.calendarTagDao = calendarTagDao;
        this.placeDao = placeDao;
        this.routeSearchDao = routeSearchDao;
        this.favouritePlaceDao = favouritePlaceDao;
    }

    /**
     * Creates the RequestHandler.
     *
     * @param context for AddressConverter
     * @param mobilityProfile Journey planner that provides the logic for our app
     * @param calendarTagDao DAO for calendar tags
     * @param placeDao DAO for visits
     * @param routeSearchDao DAO for routeSearch
     * @param favouritePlaceDao DAO for favourite places
     */
    public RequestHandler(Context context, DestinationLogic mobilityProfile, CalendarTagDao calendarTagDao,
                          PlaceDao placeDao, RouteSearchDao routeSearchDao, FavouritePlaceDao favouritePlaceDao) {
        this.context = context;
        this.mobilityProfile = mobilityProfile;
        this.calendarTagDao = calendarTagDao;
        this.placeDao = placeDao;
        this.routeSearchDao = routeSearchDao;
        this.favouritePlaceDao = favouritePlaceDao;
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d("Remote Service", "Remote Service invoked (" + msg.what + ")");

        Message message;
        switch (msg.what) {
            case REQUEST_MOST_LIKELY_DESTINATION:
                message = processDestinationRequest();
                break;
            case SEND_USED_ROUTE:
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
            Log.d("Remote Service", "Invocation failed!");
        }
    }

    /**
     * Returns a message with data that tells the most likely destination calculated in Mobility Profile.
     * @return Response message
     */
  /*  private Message processDestinationRequest() {
        return createMessage(RESPOND_MOST_LIKELY_DESTINATION, mobilityProfile.getMostLikelyDestination(getStartLocation()));
    }
*/
    /**
     * Returns a message with data that tells the most likely destinations calculated in Mobility Profile.
     * @return Response message
     */
    private Message processDestinationRequest() {
        return createMessage(RESPOND_MOST_LIKELY_DESTINATION, mobilityProfile.getListOfMostLikelyDestinations(getStartLocation()));
    }

    /**
     * Processes used route by adding it to calendar tags and used routes.
     *
     * @param message Message with data that tells which destination the user inputted
     */
    private void processUsedRoute(Message message) {
        Bundle bundle = message.getData();
        StringTokenizer tokenizer = new StringTokenizer(bundle.getString(SEND_USED_ROUTE+""));
        String startLocation = tokenizer.nextToken();
        String destination = tokenizer.nextToken();

        List<Suggestion> suggestions = mobilityProfile.getLatestSuggestions();
        for (Suggestion suggestion : suggestions) {
            if (suggestion.getSource() == SuggestionSource.CALENDAR_SUGGESTION) {
                CalendarTag calendarTag = new CalendarTag(suggestion.getDestination(), destination);
                calendarTagDao.insertCalendarTag(calendarTag);
            }
        }

        RouteSearch routeSearch = new RouteSearch(System.currentTimeMillis(), startLocation, destination);
        routeSearchDao.insertRouteSearch(routeSearch);
        
        FavouritePlace fav = favouritePlaceDao.findFavouritePlaceByAddress(destination);
        if (fav != null) {
            fav.increaseCounter();
        }
    }

    /**
     * Returns the start location a.k.a the last known location of user.
     *
     * @return Start location address
     */
    private String getStartLocation() {
        Place lastKnownPlace = placeDao.getLatestVisit();
        if (lastKnownPlace == null) {
            // TODO something better
            return "None";
        } else {
            return "Kumpula";
        }
    }


    /**
     * Creates an error message.
     *
     * @param code Message code
     * @return Error message
     */
    private Message processErrorMessage(int code) {
        return createMessage(ERROR_UNKNOWN_CODE, code+"");
    }

    /**
     * Creates a message with the given code and info.
     *
     * @param code Message code
     * @param info Message information
     * @return Created message
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
     * Creates a message that has a list of strings as info.
     *
     * @param code Message code
     * @param info List of info strings
     * @return Created message
     */
    private Message createMessage(int code, ArrayList<String> info) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(code+"", info);
        Message message = Message.obtain(null, code);
        message.setData(bundle);

        return message;
    }

    /**
     * Returns a message that contains information of user's favorite places.
     *
     * @return User's favourite places as message
     */
    private Message getFavouritePlaces() {
        return createMessage(RESPOND_FAVOURITE_PLACES, favouritePlaceDao.getNamesOfFavouritePlaces());
    }
}
