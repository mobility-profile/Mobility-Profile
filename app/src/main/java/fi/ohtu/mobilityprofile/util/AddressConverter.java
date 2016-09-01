package fi.ohtu.mobilityprofile.util;

import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;

import fi.ohtu.mobilityprofile.MainActivity;
import fi.ohtu.mobilityprofile.domain.Coordinate;

/**
 * This class is used for converting GPS coordinates to an actual address and save that address to the database.
 */
public class AddressConverter {
    /**
     * Converts coordinates to an address. Returns null if no address was found.
     *
     * @param coordinate Coordinates to be converted
     * @return Converted address
     */
    public static Address getAddressForCoordinates(Coordinate coordinate) {
        try {
            Geocoder geocoder = new Geocoder(MainActivity.getContext());
            List<Address> addresses = geocoder.getFromLocation(coordinate.getLatitude(), coordinate.getLongitude(), 1);
            if (addresses.size() >= 1) {
                return addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts an address to coordinates. Returns null if no coordinates were found.
     *
     * @param address Address to be converted
     * @return Converted coordinates
     */
    public static Coordinate getCoordinatesFromAddress(String address) {
        try {
            Geocoder geocoder = new Geocoder(MainActivity.getContext());
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() >= 1) {
                return new Coordinate((float) addresses.get(0).getLatitude(), (float) addresses.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getCountryCode(String address) {
        try {
            Geocoder geocoder = new Geocoder(MainActivity.getContext());
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() >= 1) {
                return addresses.get(0).getCountryCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}