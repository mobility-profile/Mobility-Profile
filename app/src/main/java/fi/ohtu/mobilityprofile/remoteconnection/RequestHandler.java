package fi.ohtu.mobilityprofile.remoteconnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import fi.ohtu.mobilityprofile.data.GpsPointDao;
import fi.ohtu.mobilityprofile.data.TransportModeDao;
import fi.ohtu.mobilityprofile.domain.GPSPoint;
import fi.ohtu.mobilityprofile.data.InterCitySearchDao;
import fi.ohtu.mobilityprofile.domain.InterCitySearch;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.domain.CalendarTag;
import fi.ohtu.mobilityprofile.data.CalendarTagDao;
import fi.ohtu.mobilityprofile.domain.FavouritePlace;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;

import static fi.ohtu.mobilityprofile.remoteconnection.RequestCode.*;

/**
 * Used for processing incoming requests from other apps.
 */
public class RequestHandler extends Handler {
    private DestinationLogic destinationLogic;

    /**
     * Creates the RequestHandler.
     *
     * @param destinationLogic Journey planner that provides the logic for our app
     */
    public RequestHandler(DestinationLogic destinationLogic) {
        this.destinationLogic = destinationLogic;
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d("Remote Service", "Remote Service invoked (" + msg.what + ")");

        Message message;
        switch (msg.what) {
            case REQUEST_INTRA_CITY_SUGGESTIONS:
                message = processIntraCitySuggestionsRequest();
                break;
            case REQUEST_INTER_CITY_SUGGESTIONS:
                message = processInterCitySuggestionsRequest();
                break;
            case SEND_SEARCHED_ROUTE:
                processUsedRoute(msg);
                return;
            case RESPOND_FAVOURITE_PLACES:
                message = getFavouritePlaces();
                break;
            case REQUEST_TRANSPORT_PREFERENCES:
                message = getTransportPreferences();
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
     * Returns a message with data that tells the most likely destinations calculated in Mobility Profile.
     *
     * @return Response message
     */
    private Message processIntraCitySuggestionsRequest() {
        return createMessage(RESPOND_MOST_LIKELY_DESTINATION, destinationLogic.getListOfIntraCitySuggestions(getStartLocation()));
    }

    private Message processInterCitySuggestionsRequest() {
        return createMessage(RESPOND_MOST_LIKELY_DESTINATION, destinationLogic.getListOfInterCitySuggestions(getStartLocation()));
    }

    /**
     * Processes used route by adding it to calendar tags and used routes.
     *
     * @param message Message with data that tells which destination the user inputted
     */
    private void processUsedRoute(Message message) {
        Bundle bundle = message.getData();
        StringTokenizer tokenizer = new StringTokenizer(bundle.getString(SEND_SEARCHED_ROUTE + ""), "|");
        if (tokenizer.countTokens() != 2) {
            Log.d("Request Handler", "Invalid parameters when processing used route");
            return;
        }

        String startLocation = tokenizer.nextToken();
        String destination = tokenizer.nextToken();

        switch (destinationLogic.getLatestSuggestionType()) {
            case DestinationLogic.INTRA_CITY_SUGGESTION:
                processIntraCityRoute(startLocation, destination);
                break;
            case DestinationLogic.INTER_CITY_SUGGESTION:
                processInterCityRoute(startLocation, destination);
                break;
        }
    }

    private void processIntraCityRoute(String startLocation, String destination) {
        List<Suggestion> suggestions = destinationLogic.getLatestSuggestions();
        for (Suggestion suggestion : suggestions) {
            if (suggestion.getSource() == SuggestionSource.CALENDAR_SUGGESTION) {
                CalendarTag calendarTag = new CalendarTag(suggestion.getDestination(), destination);
                CalendarTagDao.insertCalendarTag(calendarTag);
            }
        }

        System.out.println("AAA " + startLocation + "   " + destination);
        RouteSearch routeSearch = new RouteSearch(System.currentTimeMillis(), startLocation, destination);
        RouteSearchDao.insertRouteSearch(routeSearch);
        
        FavouritePlace fav = FavouritePlaceDao.findFavouritePlaceByAddress(destination);
        if (fav != null) {
            fav.increaseCounter();
        }
    }

    private void processInterCityRoute(String startLocation, String destination) {
        InterCitySearch interCitySearch = new InterCitySearch(startLocation, destination, System.currentTimeMillis());
        InterCitySearchDao.insertInterCitySearch(interCitySearch);
    }

    /**
     * Returns the start location a.k.a the last known location of user.
     *
     * @return Start location address
     */
    private GPSPoint getStartLocation() {
        GPSPoint lastKnownGPSPoint = GpsPointDao.getLatest();
        if (lastKnownGPSPoint == null) {
            // TODO something better
            return null;
        } else {
            return lastKnownGPSPoint;
        }
    }

    private Message getTransportPreferences() {
        return createMessage(RESPOND_TRANSPORT_PREFERENCES, TransportModeDao.getNamesOfPreferredTransportModes());
    }

    /**
     * Creates an error message.
     *
     * @param code Message code
     * @return Error message
     */
    private Message processErrorMessage(int code) {
        return createMessage(ERROR_UNKNOWN_CODE, code + "");
    }

    /**
     * Creates a message with the given code and info.
     *
     * @param code Message code
     * @param info Message information
     * @return Created message
     */
    private Message createMessage(int code, String info) {
        // Setup the reply message
        Bundle bundle = new Bundle();
        bundle.putString(code + "", info);
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
    private Message createMessage(int code, List<String> info) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(code + "", (ArrayList<String>) info);
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
        return createMessage(RESPOND_FAVOURITE_PLACES, FavouritePlaceDao.getNamesOfFavouritePlaces());
    }
}
