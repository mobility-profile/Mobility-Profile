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
import fi.ohtu.mobilityprofile.data.StartLocationDao;
import fi.ohtu.mobilityprofile.data.TransportModeDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.data.InterCitySearchDao;
import fi.ohtu.mobilityprofile.domain.InterCitySearch;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.suggestions.Suggestion;
import fi.ohtu.mobilityprofile.suggestions.SuggestionSource;
import fi.ohtu.mobilityprofile.util.AddressConverter;

import static fi.ohtu.mobilityprofile.remoteconnection.RequestCode.*;

/**
 * Used for processing incoming requests from other apps.
 */
public class RequestHandler extends Handler {
    private Context context;
    private DestinationLogic destinationLogic;

    /**
     * Creates the RequestHandler.
     *
     * @param context context
     * @param destinationLogic Journey planner that provides the logic for our app
     */
    public RequestHandler(Context context, DestinationLogic destinationLogic) {
        this.context = context;
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
     * Returns a message with data that contains the most likely destinations within cities.
     *
     * @return Response message
     */
    private Message processIntraCitySuggestionsRequest() {
        return createMessage(RESPOND_MOST_LIKELY_DESTINATION, destinationLogic.getListOfIntraCitySuggestions(getStartLocation()));
    }

    /**
     * Returns a message with data that contains the most likely destinations between cities.
     * @return response message
     */
    private Message processInterCitySuggestionsRequest() {
        return createMessage(RESPOND_MOST_LIKELY_DESTINATION, destinationLogic.getListOfInterCitySuggestions(getStartLocation()));
    }

    /**
     * Processes searched route by adding it to database.
     *
     * @param message Message with data that tells which destination the user inputted
     */
    private void processUsedRoute(Message message) {
        Bundle bundle = message.getData();
        StringTokenizer tokenizer = new StringTokenizer(bundle.getString(SEND_SEARCHED_ROUTE + ""), "|");
        if (tokenizer.countTokens() != 2) {
            Log.d("Request Handler", "Invalid parameters when processing searched route");
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

    /**
     * Processes searched intraCityRoutes by inserting them to the database.
     * @param startLocation starting location of the route
     * @param destination destination of the route
     */
    private void processIntraCityRoute(Place startLocation, Place destination) {
        Coordinate startCoordinate = AddressConverter.getCoordinatesFromAddress(context, startLocation);
        Coordinate endCoordinate = AddressConverter.getCoordinatesFromAddress(context, destination);

        if (startCoordinate != null && endCoordinate != null) {
            RouteSearch routeSearch = new RouteSearch(System.currentTimeMillis(), startLocation, destination);
            RouteSearchDao.insertRouteSearch(routeSearch);
        }
    }

    /**
     * Processes used interCityRoutes by inserting them to the database.
     * @param startLocation starting location of the route
     * @param destination destination of the route
     */
    private void processInterCityRoute(String startLocation, String destination) {
        InterCitySearch interCitySearch = new InterCitySearch(startLocation, destination, System.currentTimeMillis());
        InterCitySearchDao.insertInterCitySearch(interCitySearch);
    }

    /**
     * Returns the start location a.k.a the last known location of user.
     *
     * @return Start location address
     */
    private StartLocation getStartLocation() {
        StartLocation lastKnownGpsPoint = StartLocationDao.getStartLocation();
        if (lastKnownGpsPoint == null || lastKnownGpsPoint.getCoordinate() == null) {
            // TODO: Something better
            return new StartLocation(System.currentTimeMillis(), 0, 0f, 0f);
        }

        return lastKnownGpsPoint;
    }

    private Message getTransportPreferences() {
        return createMessage(RESPOND_TRANSPORT_PREFERENCES, TransportModeDao.getNamesOfPreferredTransportModes().toString());
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
        return createMessage(RESPOND_FAVOURITE_PLACES, PlaceDao.getFavouritePlacesInJson());
    }
}
