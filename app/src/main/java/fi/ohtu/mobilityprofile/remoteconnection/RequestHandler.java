package fi.ohtu.mobilityprofile.remoteconnection;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fi.ohtu.mobilityprofile.MainActivity;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.StartLocationDao;
import fi.ohtu.mobilityprofile.data.TransportModeDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.domain.RouteSearch;
import fi.ohtu.mobilityprofile.domain.StartLocation;
import fi.ohtu.mobilityprofile.suggestions.DestinationLogic;
import fi.ohtu.mobilityprofile.data.RouteSearchDao;
import fi.ohtu.mobilityprofile.suggestions.locationHistory.GpsPointClusterizer;
import fi.ohtu.mobilityprofile.util.AddressConverter;

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
            case REQUEST_SUGGESTIONS:
                message = processSuggestionsRequest(msg.getData());
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
                message = processUnknownCode(msg.what);
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
     * @param data intracity or intercity
     * @return Response message
     */
    private Message processSuggestionsRequest(Bundle data) {
        int mode = data.getInt("mode");

        switch (mode) {
            case MODE_INTRACITY:
                return createMessage(RESPOND_MOST_LIKELY_DESTINATION, destinationLogic.getListOfIntraCitySuggestions(getStartLocation()));
            case MODE_INTERCITY:
                return createMessage(RESPOND_MOST_LIKELY_DESTINATION, destinationLogic.getListOfInterCitySuggestions(getStartLocation()));
        }

        return createMessage(ERROR_UNKNOWN_MODE, "");
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
        Coordinate start = new Coordinate(bundle.getFloat("startLat"), bundle.getFloat("startLon"));
        Coordinate end = new Coordinate(bundle.getFloat("endLat"), bundle.getFloat("endLon"));
        Place startLocation = PlaceDao.getPlaceClosestTo(start);
        Place destination = PlaceDao.getPlaceClosestTo(end);

        if(startLocation == null || startLocation.distanceTo(start) > GpsPointClusterizer.CLUSTER_RADIUS) {
            Address address = AddressConverter.getAddressForCoordinates(start);
            startLocation = new Place(address.getAddressLine(0), address);
            PlaceDao.insertPlace(startLocation);
        }
        if(destination == null || destination.distanceTo(end) > GpsPointClusterizer.CLUSTER_RADIUS) {
            Address address = AddressConverter.getAddressForCoordinates(end);

            destination = new Place(address.getAddressLine(0), address);
            PlaceDao.insertPlace(destination);
        }

        RouteSearch routeSearch = new RouteSearch(System.currentTimeMillis(), bundle.getInt("mode"), startLocation, destination);
        RouteSearchDao.insertRouteSearch(routeSearch);
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
    private Message processUnknownCode(int code) {
        return createMessage(ERROR_UNKNOWN_REQUEST, code + "");
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
