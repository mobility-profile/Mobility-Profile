package fi.ohtu.mobilityprofile.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;

import fi.ohtu.mobilityprofile.domain.Coordinate;

/**
 * This class is used for converting GPS coordinates to an actual address and save that address to the database.
 */
public class AddressConverter {
    /**
     * Converts coordinates to an address. Returns null if no address was found.
     *
     * @param context Context for the geocoder
     * @param coordinate Coordinates to be converted
     * @return Converted address
     */
    public static String convertToAddress(Context context, Coordinate coordinate) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocation(coordinate.getLatitude(), coordinate.getLongitude(), 1);
            if (addresses.size() >= 1) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Converts an address to coordinates. Returns null if no coordinates were found.
     *
     * @param context Context for the geocoder
     * @param address Address to be converted
     * @return Converted coordinates
     */
    public static Coordinate convertToCoordinates(Context context, String address) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() >= 1) {
                return new Coordinate((float) addresses.get(0).getLatitude(), (float) addresses.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getCountryCode(Context context, String address) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() >= 1) {
                return addresses.get(0).getCountryCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getCity(Context context, Coordinate coordinate) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocation(coordinate.getLatitude(), coordinate.getLongitude(), 1);
            if (addresses.size() >= 1) {
                return addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}